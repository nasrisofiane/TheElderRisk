import React from 'react';
import './player_turn.css';
import './body_map.css';
export default class PlayerTurn extends React.Component{

    state = {isLoaded : true, playerTurn : null}

    // async componentDidMount(){
    //     try{
    //         let response = await fetch(`http://localhost:8080/playerturn`);
    //         if(response.ok){
    //             let data = await response.json()
    //             this.setState({
    //                 isLoaded : true,
    //               playerTurn : data.name
    //             })
    //             console.log(data.name);
    //             throw new Error(response.statusText);
    //         }
    //     }
    //     catch(err){
    //     }
    // }

    render(){
        return(
            <div className="player-turn">
               <div id="players_colors">{this.props.players.map((player) => { return <div className="container_case">{player.name} <div className={"case_color player_"+player.id}></div></div>})}</div> 
                <h1>{this.state.isLoaded ? "Player's turn "+this.props.playerturn : "Loading player's turn"}</h1>
            </div>
        );
    }
}