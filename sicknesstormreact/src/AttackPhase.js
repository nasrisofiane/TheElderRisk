import React from 'react';
import AllTerritories from './AllTerritories';
import './attack_phase.css';

export default class AttackPhase extends React.Component{
    state = {isLoaded : false, diceOne : 1, diceTwo: 1, territoryAttacker: null, territoryDefender:null, resultLastFight: null};

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

    territoriesInputs = (territorySelected, listName) => {
        if(listName == "attackers"){
            this.setState({territoryAttacker : territorySelected.value});
        }
        if(listName == "defenders"){
            this.setState({territoryDefender : territorySelected.value});
        }
    }

     sendAttackToServer = async () =>{
        if(this.state.territoryAttacker != null && this.state.territoryDefender != null && this.state.diceOne != null && this.state.diceTwo != null){
            try{
                let response = await fetch(`http://localhost:8080/fight/${this.state.territoryAttacker}/${this.state.territoryDefender}/${this.state.diceOne}/${this.state.diceTwo}`);
                if(response.ok){
                    let data = await response.text()
                    this.setState({isLoaded : true,resultLastFight : data});
                    console.log(this.state.resultLastFight);
                    throw new Error(response.statusText);
                }
                    
            }
            catch(err){
                console.log(err);
                
            }
        }
        else{
            alert("Be sure that you've well selected both territory.");
        }
        
    }
    
    render(){
        return(
            <div className="container-attack-phase">
                <div className="attack-phase">
                    <AllTerritories action={this.territoriesInputs} id="list-attackers" name="attackers"/>
                    <AllTerritories action={this.territoriesInputs} id="list-defenders" name="defenders"/>
                    <div className="dices-container">
                        <h3>Dices</h3>
                        <input id="dice-one" type="number" value={this.state.diceOne} max="3" min="1" onChange={this.dicesInputs}/>
                        <input id="dice-two" type="number" value={this.state.diceTwo} max="2" min="1" onChange={this.dicesInputs}/>
                    </div>
                    <button id="fight-button" onClick={this.sendAttackToServer}>Fight</button>
                </div>
                <h2>{this.state.isLoaded ? this.state.data : "Loading.."}</h2>
            </div>
        );
    }
}