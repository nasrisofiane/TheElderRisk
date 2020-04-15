package com.web.theelderrisk;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TheElderRiskController {
	@Autowired
	TheElderRiskService theElderRiskService;
	
	@Autowired
	SimpMessageSendingOperations messageTemplate;

	private List<Game> games = new ArrayList<>();

	/**
	 * Function that creates a single Task with actions to perform once the task is excuted
	 * @param game
	 * @return TimerTask
	 */
	private TimerTask createTask(Game game){
		//Controller in a var to pass it in the scope of the TimerTask Class
		TheElderRiskController controller = this;

		TimerTask newTask = new TimerTask () {
			@Override
		   	public void run () {
				if(game != null && game.isGameHasStarted() == true){
					game.executeOnTask();
					controller.createTimer(game);
				}
				cancel();
			}
		};
		this.sendGameToMessageQueue(game);
		return newTask;
	}

	/**
	 * Call a method in service to schedule a task.
	 * @param game
	 */
	public void createTimer(Game game){
		theElderRiskService.createTimerTask(game, this.createTask(game));
		this.sendGameToMessageQueue(game);
	}
	
	@GetMapping("/getAllGames")
	public List<Game> getGames(){
		return this.games;
	}

	/**
	 * Create a number of games that can be joined
	 */
	@PostConstruct
	public void setGames() {
		for(int i = 0; i < 3; i++){
			Game game = new Game();
			game.setId(this.games.size());
			this.games.add(game);
		}
	}

	/**
	 * Create a new game if there's less than 3 games available.
	 */
	private void checkHasEnoughGamesAvailable(){

		if(this.listOfAvailableGames().size() < 3){
			Game game = new Game();
			game.setId(this.games.size());
			this.games.add(game);
		}
	}

	/**
	 * 
	 * @return A list of available games
	 */
	private List<Game> listOfAvailableGames(){
		List<Game> availableGames = new ArrayList<>();

		for(Game game : this.games){
			if(game.getPlayerList().size() < 3 && !game.isGameHasStarted()){
				availableGames.add(game);
			}
		}
		return availableGames;
	}
	
	/**
	 * Creates headers for a session Id passed in params
	 * @param sessionId
	 * @return the headers created for the Session Id
	 */
	private MessageHeaders createHeaders(String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
	}
	
	@EventListener
	private void onConnectEvent(SessionConnectEvent event) {
		String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
		messageTemplate.convertAndSendToUser(sessionId, "/queue/message", this.listOfAvailableGames(), this.createHeaders(sessionId));
		checkHasEnoughGamesAvailable();
	}
	
	@EventListener
	private void onSubscribeEvent(SessionSubscribeEvent event) {
		String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
		messageTemplate.convertAndSendToUser(sessionId, "/queue/message", this.listOfAvailableGames(), this.createHeaders(sessionId));
	}
	
	@EventListener
	public void onDisconnectEvent(SessionDisconnectEvent event) {
		for(Game game : this.games){

			for(Player player : game.getPlayerList()){
				if(player.getSessionId().equals(event.getSessionId())){
					
					this.theElderRiskService.deleteDisconnectedPlayer(game, player.getSessionId());
					this.sendGameToMessageQueue(game);
					break;
				}
			}

			if(game.getPhase() == GamePhase.END){
				this.sendGameToMessageQueue(game);
				this.games.remove(game);
				break;
			}
		}
	}	

	/**
	 * Method that return a game from the games list and check if the sessionId correspond to one player that is part of the game
	 * @param gameId
	 * @return the game found in games list
	 */
	private Game findCorrespondingGame(int gameId){
		Game gameFound = null;

		for(Game game : this.games){
			if(game.getId() == gameId){
				gameFound = game;
				break;
			}
		}

		for(Player player : gameFound.getPlayerList()){
			if(player.getSessionId().equals(SimpAttributesContextHolder.currentAttributes().getSessionId())){
				return gameFound;
			}
		}

		return gameFound;
	}

	/**
	 * Send game object passed in params to all players that is part of the game.
	 * @param game
	 */
	private void sendGameToMessageQueue(Game game){
		for(Player player : game.getPlayerList()){
			messageTemplate.convertAndSendToUser(player.getSessionId(), "/queue/message", game, this.createHeaders(player.getSessionId()));
		}
	}

	/**
	 * Send PlayersList object passed in params to all players that is part of the game.
	 * @param game
	 */
	private void sendPlayersToMessageQueue(Game game){
		for(Player player : game.getPlayerList()){
			messageTemplate.convertAndSendToUser(player.getSessionId(), "/queue/message", game.getPlayerList(), this.createHeaders(player.getSessionId()));
		}
	}
	
	/**
	 * Get a game with the id of the game passed in param
	 * @param gameId
	 */
	@MessageMapping("/message/{gameId}")
  	public void getGame(@DestinationVariable int gameId) {
		Game game = this.findCorrespondingGame(gameId);
		sendGameToMessageQueue(game);
	}	
	
	/**
	 * Connect to a game that correspond to his ID, if the game has less than 3 players that joined the game and
	 * the game has not started, the player is able to join, else, nothing happens
	 * 
	 * If the player has successfully joined the game, the user will receive all informations of the joined game and a 
	 * message will be sent to all players with the new informations of the available games. 
	 * @param gameId
	 */
	@MessageMapping("/connectToGame/{gameId}")
	public void connectToGame(@DestinationVariable int gameId){

		String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
		Game gameFound = null;

		for(Game game : this.games){
			if(game.getId() == gameId){
				gameFound = game;
				break;
			}
		}
		
		if(gameFound != null && gameFound.getPlayerList().size() < 3 && !gameFound.isGameHasStarted()){
			Player player = new Player();
			player.setId(gameFound.getPlayerList().size()+1);
			player.setGameId(gameFound.getId());
			theElderRiskService.addPlayer(player, gameFound, sessionId);
			this.sendGameToMessageQueue(gameFound);
			messageTemplate.convertAndSend("/topic/message", this.listOfAvailableGames());
		}
		
	}
	
	/**
	 * Add a playerName to the user's informations, and send back a message to all players of the game with the updated game informations.
	 * @param namePlayer
	 * @param gameId
	 * @param headerAccessor
	 */
  	@MessageMapping("/getPlayerJoined/{gameId}")
  	public void getPlayerJoined(String namePlayer, @DestinationVariable int gameId, SimpMessageHeaderAccessor headerAccessor){
		Game game = this.findCorrespondingGame(gameId);

		if(game != null){
			for(Player player : game.getPlayerList()){
				if(player.getSessionId().equals(headerAccessor.getSessionId())){
					player.setName(namePlayer);
					theElderRiskService.savePlayer(player);
					this.sendPlayersToMessageQueue(game);
					break;
				}
			}
		}
  	}
	
	/**
	 * Move pawns from a territory to another, this method retrieve the informations of the player, dispatch it correctly 
	 * to correspond to the service method parameters.
	 * @param message
	 * @param gameId
	 */
	@MessageMapping("/movefortify/{gameId}")
	public void moveFortify(List<Integer> message, @DestinationVariable int gameId) {
		Game game = this.findCorrespondingGame(gameId);	

		//First param : territory to substracts pawns
		//Second param : territory to adds panws
		//Third param : Number of pawns
		//Fourth param : game that will be affected
		if(game != null){
			theElderRiskService.movePawns(message.get(0), message.get(1), message.get(2), game);
			this.sendGameToMessageQueue(game);
		}
		
	}
	
	/**
	 * Adds paws to a territory in the game passed in params
	 * This method dispatch the request param correctly to correspond to the service methods parameters.
	 * @param message
	 * @param gameId
	 */
	@MessageMapping("/addpawn/{gameId}")
	public void addPawn(List<Integer> message, @DestinationVariable int gameId) {
		Game game = this.findCorrespondingGame(gameId);
		
		if(game != null){
			//First param : Territory Id
			//Second param : Number of pawns to adds
			//Third param : game that will be affected
			//Fourth param : Task to executed at the end of the round
			theElderRiskService.addPawn(message.get(0), message.get(1), game, this.createTask(game));
			this.createTimer(game);
			this.sendGameToMessageQueue(game);
		}
		
	}
	
	/**
	 * Create a fight request to another player,
	 * @param message
	 * @param gameId
	 */
	@MessageMapping("/fight/{gameId}")
	public void startFight(List<Integer> message, @DestinationVariable int gameId) {
		Game game = this.findCorrespondingGame(gameId);

		if(game != null){
			theElderRiskService.buildFight(message, game);
			this.sendGameToMessageQueue(game);
		}
	}
	
	/**
	 * Method that will handle the response to the fight request created with the method Fight.
	 * @param message
	 * @param gameId
	 */
	@MessageMapping("/answerfight/{gameId}")
	public void answerFight(int message, @DestinationVariable int gameId) {
		Game game = this.findCorrespondingGame(gameId);

		if(game != null){
			theElderRiskService.answerFight(message, game);
			this.sendGameToMessageQueue(game);
		}
	}
	
	/**
	 * Method that start the game only if the first player that joined the game made requested it
	 * if it's the case then check if everybody is ready to play, if all the cases success then the game can start,
	 * send the game's informations to all player of the game passed in params.
	 * @param sessionId
	 * @param gameId
	 */
	@MessageMapping("/initializegame/{gameId}")
	public void initializeGame(String sessionId, @DestinationVariable int gameId){
		Game game = this.findCorrespondingGame(gameId);
		
		if(game != null && !game.isGameHasStarted() && game.getPlayerList().get(0).getSessionId().equals(sessionId) && SimpAttributesContextHolder.currentAttributes().getSessionId().equals(game.getPlayerList().get(0).getSessionId())){
			boolean startGame = true;

			for(Player player : game.getPlayerList()){
				if(!player.isReadyToPlay()){
					startGame = false;
					break;
				}
			}

			if(startGame == true){
				theElderRiskService.initializeGame(game);
				this.createTimer(game);
			}
		}

		this.sendGameToMessageQueue(game);
		messageTemplate.convertAndSend("/topic/message", this.listOfAvailableGames());
	}
	
	/**
	 * Close the fight phase and pass to the last phase of the round that is "MoveFortify" and send back a message to all players of the game passed in params.
	 * @param gameId
	 */
	@MessageMapping("/closefightstep/{gameId}")
	public void closeFightStep(@DestinationVariable int gameId) {
		Game game = this.findCorrespondingGame(gameId);

		if(game != null){
			theElderRiskService.closeFightStep(game);
			this.sendGameToMessageQueue(game);
		}
	}
	
	/**
	 * Close the last phase of the game's round, check if a player loosed and send back a message to all the players from game passed in params.
	 * @param gameId
	 */
	@MessageMapping("/closemovefortifystep/{gameId}")
	public void closeMoveFortifyStep(@DestinationVariable int gameId) {
		Game game = this.findCorrespondingGame(gameId);

		if(game != null){
			this.theElderRiskService.playerEliminator(game);
			theElderRiskService.closeMoveFortifyStep(game, this.createTask(game));
			this.sendGameToMessageQueue(game);
		}
		
	}


}


