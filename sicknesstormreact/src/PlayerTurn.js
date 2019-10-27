import React from 'react';
import './player_turn.css';

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
                <h1>{this.state.isLoaded ? "Player's turn "+this.props.playerturn : "Loading player's turn"}</h1>
            </div>
        );
    }
}