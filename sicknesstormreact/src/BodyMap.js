import React from 'react';
import AttackPhase from './AttackPhase';
import MoveFortify from './MoveFortify';
import PlayerTurn from './PlayerTurn';
import PlacePawnInterface from './PlacePawnInterface';
import SvgBody from './SvgBody';
import './body_map.css';


export default class BodyMap extends React.Component{
    
    constructor(){
            super();
            this.state = {diceOne : 1, isLoaded : true,  prevTerritorySelectedAttacker:null, prevTerritorySelectedDefender:null, territoryAttackerSelected:null, territoryDefenderSelected:null, switchSelect:false, tooltip: null};
            this.handleEvent = this.handleEvent.bind(this);
    }

    cleanBodyFromSelectedTerritories = async () =>{
        this.state.territoryAttackerSelected[1].classList.remove("attacker");
        this.state.territoryDefenderSelected[1].classList.remove("defender");
    }

    displayTerritoryInfos = (territorySelected) => {
        this.setState({tooltip:territorySelected})
    }

    territorySelected = async (territory) =>{
        let found = false;
        let territoryDom = territory;
        for(let i = 0; i < this.props.territories.length; i++){
            if(territory.id == this.props.territories[i][1]){
                found = true;
                
                territory = this.props.territories[i];
                this.displayTerritoryInfos(territory);
                break;
            }
        }
        if(found == true && (this.state.switchSelect == false && (this.props.game.phase == "ATTACK" || this.props.game.phase == "MOVEFORTIFY") ) || (found == true && this.props.game.phase == "PLACEPAWN") ){
            if(this.state.prevTerritorySelectedAttacker != null){
                
                this.state.prevTerritorySelectedAttacker.classList.remove('attacker');
            }
            territoryDom.classList.add('attacker');
            await this.setState({prevTerritorySelectedAttacker:territoryDom, territoryAttackerSelected:[territory, territoryDom]});
            if(this.state.switchSelect == false && (this.props.game.phase == "ATTACK" || this.props.game.phase == "MOVEFORTIFY")){
                this.setState({switchSelect:true});
            }
        }
        else if(found == true && this.state.switchSelect == true && (this.props.game.phase == "ATTACK" || this.props.game.phase == "MOVEFORTIFY")){
            if(this.state.prevTerritorySelectedDefender != null){
                this.state.prevTerritorySelectedDefender.classList.remove('defender');
            }
            territoryDom.classList.add('defender');
            await this.setState({territoryDefenderSelected:[territory, territoryDom]});
            this.setState({switchSelect:false,  prevTerritorySelectedDefender:territoryDom});
        }
        else{
            console.log("error");
        }   
    }

    //method created to be passed in a child to call the fetch that retrieve the phase data.
    handleEvent = async () => {
        await this.props.sendMessage();
        console.log("event sent");
    }

    answerToAttack = async () =>{
        this.props.sendMessageToAnswerFight(this.state.diceOne);
    }

    dicesInputs =({target:{id, value}}) => {
        if(id =="dice-one"){
            this.setState({diceOne:value});
            console.log("Dice One => "+ this.state.diceOne);
        }
    }
    
    render(){
        return(
            <div id="game-window">
                {this.props.game.getAttacked != null && this.props.game.getAttacked == this.props.userName ? 
                <div id="answer-attack">
                    <h2>Answer to the attack</h2>
                    <h3>Dice</h3>
                    <input id="dice-one" type="number" value={this.state.diceOne} max="3" min="1" onChange={this.dicesInputs}/>
                <button onClick={() => this.answerToAttack()}>Defend with {this.state.diceOne} pawns</button>GET ATTACKED
                    </div> : ""}
                {this.props.userName != null ? this.props.game.userName : "ERROR"}
                    {this.state.isLoaded ? <SvgBody  updateTerritories={this.props.territories} territorySelected={this.territorySelected}  ></SvgBody> : "Loading..."}
                    <section id="user-ui">
                        {this.props.game.phase != "INITIALIZE" ? <PlayerTurn playerturn={this.props.game.playerTurn.name} players={this.props.players}/> : ""}
                        {this.state.tooltip != null ? <div id="territory-infos"><p><i>Territory selected infos</i></p><h4><strong>{this.state.tooltip[1]}</strong></h4> <p>Number of pawns : {this.state.tooltip[2]}</p> <p>Owner : {this.state.tooltip[3]}</p></div> : ""}
                        {this.props.userName == this.props.game.playerTurn.name ? 
                        <div id="phase-interface">
                            {this.props.game.phase != "INITIALIZE" && this.props.game.phase == "PLACEPAWN" ? <PlacePawnInterface sendMessageToAddPawns={this.props.sendMessageToAddPawns} updatephase={this.handleEvent} territoryAttackerSelected={this.state.territoryAttackerSelected}  /> : ""}
                            {this.props.game.phase != "INITIALIZE" && this.props.game.phase == "ATTACK" ? <AttackPhase userName={this.props.userName} game={this.props.game} sendMessageToCloseFightStep={this.props.sendMessageToCloseFightStep} getAttacked={this.props.game.getAttacked} sendMessageToFight={this.props.sendMessageToFight} territoryAttackerSelected={this.state.territoryAttackerSelected}  territoryDefenderSelected={this.state.territoryDefenderSelected}/> : ""}
                            {this.props.game.phase != "INITIALIZE" && this.props.game.phase == "MOVEFORTIFY" ? <MoveFortify sendMessageToMovePawns={this.props.sendMessageToMovePawns} sendMessageCloseMoveFortifyPhase={this.props.sendMessageCloseMoveFortifyPhase} cleanSelected={this.cleanBodyFromSelectedTerritories} territoryAttackerSelected={this.state.territoryAttackerSelected}  territoryDefenderSelected={this.state.territoryDefenderSelected}/> : ""}
                        </div> : "" }
                    </section>
            </div>
              
        );
    }
   
    
}