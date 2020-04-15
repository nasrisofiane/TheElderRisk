import React from 'react';
import './attack_phase.css';
import PlayerTurn from './PlayerTurn';

export default class AttackPhase extends React.Component{
    state = {
        isLoaded : false,
        diceOne : 1,
        territoryAttacker: null,
        territoryDefender:null,
        resultLastFight: null,
        openLastFightResults : false
    };

    dicesInputs =({target:{id, value}}) => {
        if(id =="dice-one"){
            this.setState({diceOne:value});
        }
    }
    
    closeAttackPhase = async () =>{
        this.props.sendMessageToCloseFightStep();
    }

    sendAttackToServer = async () =>{
        this.props.displayLastFightResults(true);
        if(this.props.territoryAttackerSelected != null && this.props.territoryDefenderSelected != null && this.state.diceOne != null){
            await this.props.sendMessageToFight(this.props.territoryAttackerSelected[0][0], this.props.territoryDefenderSelected[0][0], this.state.diceOne);
        }
        else{
            alert("Be sure that you've well selected both territory.");
        }
    }
    
    render(){
        return(
            <div className="container-attack-phase" >

                 {this.props.getAttacked != null && this.props.game.playerTurn.name == this.props.userName ? <div className={"infos-phase-popup "}>
                    <div className="form-phase">
                        <div id="preview_fight"><p id="attacker-choice">{this.props.territoryAttackerSelected != null ? this.props.territoryAttackerSelected[0][1] :"Select a territory"} </p>VS <p id="defender-choice">{this.props.territoryDefenderSelected != null ? this.props.territoryDefenderSelected[0][1] :"Select a territory"} </p> </div>
                        <p>WAITING DEFENDER'S ANSWER</p>
                    </div>
                </div> : ""}
                <div style={this.state.styles}>
                    
                    {this.props.getAttacked == null ? <div className="attack-interface-beforepopup">
                        <div className="attack-phase">
                        <h2>Attack phase</h2>
                            <div className="dices-container">
                                <h3>Dices</h3>
                                <input id="dice-one" type="number" value={this.state.diceOne} max="3" min="1" onChange={this.dicesInputs}/>
                            </div>
                            
                            <button id="fight-button" onClick={this.sendAttackToServer}>Fight</button>
                            <button id="close-fight-button" onClick={this.closeAttackPhase}>Go to next phase (move pawns)</button>
                        </div>
                    </div> : ""}
                </div>
            </div> 
        );
    }
}

