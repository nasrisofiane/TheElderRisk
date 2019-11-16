import React from 'react';
import './attack_phase.css';

export default class AttackPhase extends React.Component{
    state = {isLoaded : false, diceOne : 1, territoryAttacker: null, territoryDefender:null, resultLastFight: null};

    dicesInputs =({target:{id, value}}) => {
        if(id =="dice-one"){
            this.setState({diceOne:value});
            console.log("Dice One => "+ this.state.diceOne);
        }
    }

    conponentDidUpdate = async () =>{
        console.log(this.props.territoryAttackerSelected);
    }

    closeAttackPhase = async () =>{
        this.props.sendMessageToCloseFightStep();
    }

    sendAttackToServer = async () =>{
        if(this.props.territoryAttackerSelected != null && this.props.territoryDefenderSelected != null && this.state.diceOne != null){
            await this.props.sendMessageToFight(this.props.territoryAttackerSelected[0][0], this.props.territoryDefenderSelected[0][0], this.state.diceOne);
        }
        else{
            alert("Be sure that you've well selected both territory.");
        }
    }


    renderFight(resultLastFight){
        if(typeof resultLastFight ==  "string"){
            return <div>{resultLastFight}</div>;
        }else{
            const results = this.state.resultLastFight.map((dicesResults) => { return [dicesResults]});
            const jsxRenderResultsWonDices = [];
            const jsxRenderResultsDiceAttack = [];
            const jsxRenderResultsDiceDefender = [];
            
            for(var i = 0; i < results[1].length; i++){
                jsxRenderResultsDiceAttack.push(results[1][i]);
            }

            for(var i = 0; i < results[2].length; i++){
                jsxRenderResultsDiceDefender.push(results[2][i]);
            }

            jsxRenderResultsWonDices.push(results[0][0]);
            jsxRenderResultsWonDices.push(results[0][1]);
            
            console.log(jsxRenderResultsWonDices);
            console.log(jsxRenderResultsDiceAttack);
            return(
                <div> 
                        <h2>Attacker Dices results</h2>
                        {jsxRenderResultsDiceAttack[0].map((dices, index) => {return <div>Dice n°{index} -> {dices} </div> })}
                    
                    <div>
                        <h2>Defender Dices results</h2>
                        {jsxRenderResultsDiceDefender[0].map((dices, index) => { return <div>Dice n°{index} -> {dices} </div> })}
                    </div>
                    <div>
                        <h2>Fight result</h2>
                        <div>Attacker loosed {jsxRenderResultsWonDices[0][1]} pawn(s) </div>
                        <div>Defender loosed {jsxRenderResultsWonDices[0][0]} pawn(s) </div>
                    </div>
                </div>
            );
        }
    }
    
    render(){
        return(
            <div className="container-attack-phase" >
                 {this.props.getAttacked != null ? <div className={"infos-phase-popup "}>
                    <div className="form-phase">
                        <div className="fight-infos">{this.state.isLoaded ? this.renderFight(this.state.resultLastFight): "Fight logs"}</div>
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

