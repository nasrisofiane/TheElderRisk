import React from 'react';
import AllTerritories from './AllTerritories';
import './attack_phase.css';

export default class AttackPhase extends React.Component{
    state = {diceOne : 1, diceTwo: 1, territoryAttacker: null, territoryDefender:null};

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

    territoriesInputs = ({territorySelected:{value}}) => {
        this.setState({territoryAttacker : value});
        alert(value);
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
                    <button onClick={()=>alert("Territory attacker => "+ this.state.territoryAttacker +" | Dices => "+ this.state.diceOne +" VS "+ this.state.diceTwo)} id="fight-button">Fight</button>
                </div>
                
            </div>
        );
    }
}