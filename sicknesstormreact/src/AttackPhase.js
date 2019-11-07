import React from 'react';
import AllTerritories from './AllTerritories';
import './attack_phase.css';

export default class AttackPhase extends React.Component{
    state = {isLoaded : false, diceOne : 1, diceTwo: 1, territoryAttacker: null, territoryDefender:null, resultLastFight: null, changeClass:"hide-phase-popup", styles:{display:"block"}};

    dicesInputs =({target:{id, value}}) => {
        if(id =="dice-one"){
            this.setState({diceOne:value});
            console.log("Dice One => "+ this.state.diceOne);
        }
        if(id =="dice-two"){
            this.setState({diceTwo:value});
            console.log("Dice Two => "+ this.state.diceTwo);
        }
    }

    conponentDidUpdate = async () =>{
        console.log(this.props.territoryAttackerSelected);
    }

    closeAttackPhase = async () =>{
        try{
            let response = await fetch(`http://localhost:8080/closefightstep`);
            if(response.ok){
                let data = await response.text()
                console.log(data);
                await this.props.updatephase();
                throw new Error(response.statusText);
            }      
        }
        catch(err){ 
        }
    }

    displayPhasePopUp = () =>{
        if(this.props.territoryAttackerSelected != null && this.props.territoryDefenderSelected != null){
            if(this.state.changeClass == "hide-phase-popup"){
                this.setState({changeClass:"", styles:{display:"none"}});
            }
            else{
                this.setState({changeClass:"hide-phase-popup", styles:{display:"block"}});
            }
        }
        else{
            alert("Select a territory first");
        }
    }


    sendAttackToServer = async () =>{
        if(this.props.territoryAttackerSelected != null && this.props.territoryDefenderSelected != null && this.state.diceOne != null && this.state.diceTwo != null){
            try{
                let response = await fetch(`http://localhost:8080/fight/${this.props.territoryAttackerSelected[0][0]}/${this.props.territoryDefenderSelected[0][0]}/${this.state.diceOne}/${this.state.diceTwo}`);
                if(response.ok){
                    let data = await response.text()
                    
                    try{
                        if(Array.isArray(JSON.parse(data))){
                            var dataJson = JSON.parse(data);
                            if(dataJson.length == 0){
                                this.setState({isLoaded : false, resultLastFight : null});
                            }
                            else{
                                this.setState({isLoaded : true, resultLastFight : dataJson});
                            }
                            
                        }
                    }
                    catch(e){
                        this.setState({isLoaded : true, resultLastFight : data});
                    }
                    await this.props.updatephase();
                    console.log(data);
                    console.log(JSON.parse(data));
                    
                    throw new Error(response.statusText);
                }       
            }
            catch(err){ 
            }
            this.displayPhasePopUp(); 
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
                 <div className={"infos-phase-popup "+ this.state.changeClass}>
                    <div className="form-phase">
                        <div className="fight-infos">{this.state.isLoaded ? this.renderFight(this.state.resultLastFight): "Fight logs"}</div>
                        <div id="preview_fight"><p id="attacker-choice">{this.props.territoryAttackerSelected != null ? this.props.territoryAttackerSelected[0][1] :"Select a territory"} </p>VS <p id="defender-choice">{this.props.territoryDefenderSelected != null ? this.props.territoryDefenderSelected[0][1] :"Select a territory"} </p> </div>
                        <button onClick={this.displayPhasePopUp}>Close fight logs</button>
                    </div>
                </div>
                <div style={this.state.styles}>
                    
                    <div className="attack-interface-beforepopup">
                        <div className="attack-phase">
                        <h2>Attack phase</h2>
                            <div className="dices-container">
                                <h3>Dices</h3>
                                <input id="dice-one" type="number" value={this.state.diceOne} max="3" min="1" onChange={this.dicesInputs}/>
                                <input id="dice-two" type="number" value={this.state.diceTwo} max="2" min="1" onChange={this.dicesInputs}/>
                            </div>
                            
                            <button id="fight-button" onClick={this.sendAttackToServer}>Fight</button>
                            <button id="close-fight-button" onClick={this.closeAttackPhase}>Go to next phase (move pawns)</button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

