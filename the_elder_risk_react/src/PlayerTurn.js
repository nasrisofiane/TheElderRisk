import React from 'react';
import './player_turn.css';
import './body_map.css';

export default class PlayerTurn extends React.Component{

    state = {isLoaded : true}

    render(){
        return(
            <div className="player-turn">
                <div className="round-timer-container">{this.props.game.phase == "PREPARE" ? "Preparation phase ends in " : this.props.userName == this.props.playerturn ? "Your turn ends in " : this.props.playerturn + " turn ends in " }<div>{this.props.timer} s</div> </div>
               <div id="players_colors">{this.props.players.map((player) => { return <div key={player.id} className="container_case">{player.name} <div className={"case_color player_"+player.id}></div></div>})}</div> 
                {this.props.game.phase != "PREPARE" ? <h1>{this.state.isLoaded ? "Player's turn "+this.props.playerturn : "Loading player's turn"}</h1> : ""}
            </div>
        );
    }
}