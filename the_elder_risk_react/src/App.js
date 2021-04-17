import React, { Component } from 'react';
import './App.css';
import './App-responsive.css';
import Loading from './Loading';
import Formulaire from './formulaire';
import BodyMap from './BodyMap';
import SockJsClient from 'react-stomp';
import GamesList from './GamesList';
import MessageWindow from './MessageWindow';

class App extends Component {
  constructor(props){
    super(props)
    this.myRef = React.createRef();
  }

  state = {
    canRequest : false,
    canConnect : false, 
    roundPhase:null, 
    territories:{}, 
    players:null, 
    userInfos:"null", 
    receivedMessageChange: null, 
    game:null, 
    gameId : null, 
    games:null
  }

  componentDidMount(){
    console.log('Game version : 1.0');
    this.connectToGames();
  }

  connectToGames = async () =>{
    let response = await fetch(`https://theelderrisk.herokuapp.com/getAllGames`);
    let data = await response.json()
    if(response.ok){
      this.setState({canConnect : true});
    }
    return data;
  }

  sendMessage = async (msg) => {
    this.clientRef.sendMessage(`/app/message/${this.state.gameId}`, msg);
  }

  sendMessageInitiliazePhase = async () => {
    const player = this.state.players.filter(player => this.state.userInfos == player.name)[0];

    if(player != null){
      await this.clientRef.sendMessage(`/app/initializegame/${this.state.gameId}`, player.sessionId);
    }
  }

  sendMessageAddPlayer = async (msg) => {
    this.setState({userInfos: msg});
    this.clientRef.sendMessage(`/app/getPlayerJoined/${this.state.gameId}`, msg);
  }

  sendMessageConnectToGame = async (gameId) => {
    this.clientRef.sendMessage(`/app/connectToGame/${gameId}`, "");
  }

  sendMessageCloseMoveFortifyPhase = async () => {
    this.clientRef.sendMessage(`/app/closemovefortifystep/${this.state.gameId}`, "");
  }

  sendMessageGetTerritories = async () => {
    this.clientRef.sendMessage(`/app/territories/${this.state.gameId}`, "");
  }

  sendMessageToAddPawns = async (territory, nbOfPawns) => {
    let AddPawnsInfos = [territory, nbOfPawns];
    await this.clientRef.sendMessage(`/app/addpawn/${this.state.gameId}`, JSON.stringify(AddPawnsInfos));
  }

  sendMessageToFight = async (territoryAttacker, territoryDefender, nbOfPawns) => {
    let fightInfos = [territoryAttacker, territoryDefender, nbOfPawns];
    await this.clientRef.sendMessage(`/app/fight/${this.state.gameId}`, JSON.stringify(fightInfos));
  }

  sendMessageToAnswerFight = async (nbdef) => {
    await this.clientRef.sendMessage(`/app/answerfight/${this.state.gameId}`, JSON.stringify(nbdef));
  }

  sendMessageToMovePawns = async (territoryA, territoryB, nbOfPawns) => {
    let movePawnsInfos = [territoryA, territoryB, nbOfPawns];
    await this.clientRef.sendMessage(`/app/movefortify/${this.state.gameId}`, JSON.stringify(movePawnsInfos));
  }

  sendMessageToCloseFightStep = async () =>{
    this.clientRef.sendMessage(`/app/closefightstep/${this.state.gameId}`, "");
  }

  messageReceived = async (msg) => {

    if(typeof msg.playerList != 'undefined'){
      let territoriesReceived = msg.territories.map((territories) => { return [territories.id, territories.name, territories.pawn, territories.player.name, territories.player.id]});
      msg.territories = territoriesReceived;
      await this.setState({roundPhase:msg.phase ,territories:territoriesReceived.sort(function(a, b) {return a[0] - b[0];}), players:msg.playerList, game:msg, gameId:msg.id});
    }
    else{
      this.setState({players:msg});
    }
    
        this.state.players.forEach((player) => {
          if(player.sessionId === localStorage.getItem('sessionId')){
            this.setState({userInfos : player.name});
          }
        })  

      if(msg.length){
        if(typeof msg[0].turnPlayerNumber != 'undefined'){
          await this.setState({games : msg})
        }
      }
      
  }

  render() {
    return (
      
      <div className="App">
        {this.state.canConnect ? <SockJsClient url='https://theelderrisk.herokuapp.com/theelderrisk' topics={['/topic/message', '/user/queue/message', '/user/queue/territories']} onMessage={this.messageReceived}  ref={ (client) => { this.clientRef = client }}/> : <Loading/>}
        {this.state.games != null  && this.state.game == null ? <GamesList connectToGame={this.sendMessageConnectToGame} games={this.state.games}/> : ""}
        {(this.state.roundPhase === null && this.state.games === null) ? <Loading/> : ""}
        {this.state.roundPhase != "INITIALIZE" && this.state.roundPhase != null ? <MessageWindow userName={this.state.userInfos} game={this.state.game}/> : ""}
        {this.state.roundPhase == "INITIALIZE" ? <Formulaire sendMessage={this.sendMessageAddPlayer} sendMessageInitiliazePhase={this.sendMessageInitiliazePhase} players={this.state.players} key={this.myRef} /> : ""}
        {this.state.roundPhase != "INITIALIZE" && this.state.players != null && this.state.game != null ? <BodyMap sendMessageToCloseFightStep={this.sendMessageToCloseFightStep} sendMessageToMovePawns={this.sendMessageToMovePawns} sendMessageToAnswerFight={this.sendMessageToAnswerFight} sendMessageToFight={this.sendMessageToFight} sendMessageToAddPawns={this.sendMessageToAddPawns} territories={this.state.territories} sendMessageGetTerritories={ this.sendMessageGetTerritories} sendMessage={this.sendMessage} sendMessageCloseMoveFortifyPhase={this.sendMessageCloseMoveFortifyPhase} players={this.state.players} game={this.state.game} userName={this.state.userInfos}/> : ""}
      </div>
    );
  }
}

export default App;
