import React from 'react';
import './game_list.css';

export default class GamesList extends React.Component{
    constructor(){
        super();
    }

    createListOfGames = () =>{
        return(
                <div className="games">
                    <h2>Join a game</h2>
                    {this.props.games != null ? this.props.games.map((game)=>{return game.gameHasStarted != true ? <p key={game.id}>{game.playerList.length} / 3 Players <button onClick={() => this.props.connectToGame(game.id)}>Join</button></p> :""}) : ""}
                </div>
        );
    }

    render(){
        return(
            <div className="game-list-container">
                <div className ="games-list">
                    {this.createListOfGames()}
                </div>
            </div>
        );
    }
}