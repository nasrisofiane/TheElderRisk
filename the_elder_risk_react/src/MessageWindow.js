import React from 'react';
import './message_window.css';

export default class MessageWindow extends React.Component{
    constructor(){
        super()
    }

    state = {display : true, message:null, playerTurnHasChanged : false};

    componentWillReceiveProps(nextProps){
        if(nextProps.game.playerTurn.name != this.props.game.playerTurn.name || (this.props.game.phase == "PREPARE" && nextProps.game.phase != "PREPARE")){
            this.setState({display : true , playerTurnHasChanged : true});
        }
    }

    firstPhaseMessage = () =>{
        return(
            <div>
                <div>Every player have to place their pawns.</div>
                {this.closeButton()}
            </div>
        );
    }

    playerTurn = () =>{
        setTimeout(() => {
            if(this.props.game.phase != "END"){
                this.setState({display : false, playerTurnHasChanged : false});
            }
        }, 3000);
        return(
            <div className="player-turn-message">
                {this.props.userName == this.props.game.playerTurn.name ? <p>It's your turn</p> : <p>It's {this.props.game.playerTurn.name} turn, wait until your turn.</p>}
            </div>
        );
    }

    endMessage = () =>{
        setTimeout(() => location.reload(), 5000);
        return(
            <div>
                <p>You're the winner !</p>
               <button onClick={() => location.reload()} className="window-message-close-button">Close</button>
            </div>
        );
    }

    closeButton = () =>{
        return(
            <button onClick={() => this.setState({display : false})} className="window-message-close-button">Close</button>
        );
    }

    render(){
        return(
            <div>
                {this.state.display ? 
                    <div className="message-window" onClick={ () => { this.setState({display : false, playerTurnHasChanged : false})}}>
                        {this.props.game.phase == "PREPARE" ? this.firstPhaseMessage() : ""}
                        {this.state.playerTurnHasChanged && this.props.game.phase != "END" ? this.playerTurn() : ""}
                        {this.props.game.phase == "END" ? this.endMessage() : ""}
                    </div>
                : ""}
                
            </div>
        );
    }
}