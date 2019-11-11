import React from 'react';
import './player_turn.css';
import './body_map.css';
export default class PlayerTurn extends React.Component{

    state = {isLoaded : true}

    render(){
        return(
            <div className="player-turn">
               <div id="players_colors">{this.props.players.map((player) => { return <div className="container_case">{player.name} <div className={"case_color player_"+player.id}></div></div>})}</div> 
                <h1>{this.state.isLoaded ? "Player's turn "+this.props.playerturn : "Loading player's turn"}</h1>
            </div>
        );
    }
}