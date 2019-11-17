import React from 'react';
import AttackPhase from './AttackPhase';
import MoveFortify from './MoveFortify';
import PlayerTurn from './PlayerTurn';
import PlacePawnInterface from './PlacePawnInterface';
import SvgBody from './SvgBody';


export default class BodyMap extends React.Component{
    
    constructor(){
            super();
            this.state = {diceOne : 1, isLoaded : true,  prevTerritorySelectedAttacker:null, prevTerritorySelectedDefender:null, territoryAttackerSelected:null, territoryDefenderSelected:null, switchSelect:false, tooltip: null, styles: { top: 0, left: 0}};
            this.handleEvent = this.handleEvent.bind(this);
    }
        
    async componentDidMount(){
        this.props.sendMessage();
        this.props.retrieveMySessionId(this.props.userName);
    }

    cleanBodyFromSelectedTerritories = async () =>{
        this.state.territoryAttackerSelected[1].classList.remove("attacker");
        this.state.territoryDefenderSelected[1].classList.remove("defender");
    }

    eventOnMouseMove = (e) => {
    }

    territorySelected = async (territory) =>{
        let found = false;
        let territoryDom = territory;
        for(let i = 0; i < this.props.territories.length; i++){
            if(territory.id == this.props.territories[i][1]){
                found = true;
                
                territory = this.props.territories[i];
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
            <div id="game-window" onMouseMove={  this.eventOnMouseMove}>
                {this.props.game.getAttacked != null && this.props.game.getAttacked == this.props.userName ? 
                <div id="answer-attack">
                    <h2>Answer to the attack</h2>
                    <h3>Dice</h3>
                    <input id="dice-one" type="number" value={this.state.diceOne} max="3" min="1" onChange={this.dicesInputs}/>
                <button onClick={() => this.answerToAttack()}>Defend with {this.state.diceOne} pawns</button>GET ATTACKED
                    </div> : ""}
                {this.props.userName != null ? this.props.game.userName : "ERROR"}
                
                    {this.state.tooltip != null ? this.state.tooltip : ""}
                    {this.state.isLoaded ? <SvgBody  updateTerritories={this.props.territories} territorySelected={this.territorySelected}  ></SvgBody> : "Loading..."}
                    {this.props.game.phase != "INITIALIZE" ? <PlayerTurn playerturn={this.props.game.playerTurn.name} players={this.props.players}/> : ""}
                    {this.props.userName == this.props.game.playerTurn.name ? 
                    <div id="phase-interface">
                        {this.props.game.phase != "INITIALIZE" && this.props.game.phase == "PLACEPAWN" ? <PlacePawnInterface sendMessageToAddPawns={this.props.sendMessageToAddPawns} updatephase={this.handleEvent} territoryAttackerSelected={this.state.territoryAttackerSelected}  /> : ""}
                        {this.props.game.phase != "INITIALIZE" && this.props.game.phase == "ATTACK" ? <AttackPhase userName={this.props.userName} game={this.props.game} sendMessageToCloseFightStep={this.props.sendMessageToCloseFightStep} getAttacked={this.props.game.getAttacked} sendMessageToFight={this.props.sendMessageToFight} territoryAttackerSelected={this.state.territoryAttackerSelected}  territoryDefenderSelected={this.state.territoryDefenderSelected}/> : ""}
                        {this.props.game.phase != "INITIALIZE" && this.props.game.phase == "MOVEFORTIFY" ? <MoveFortify sendMessageToMovePawns={this.props.sendMessageToMovePawns} sendMessageCloseMoveFortifyPhase={this.props.sendMessageCloseMoveFortifyPhase} cleanSelected={this.cleanBodyFromSelectedTerritories} territoryAttackerSelected={this.state.territoryAttackerSelected}  territoryDefenderSelected={this.state.territoryDefenderSelected}/> : ""}
                    </div> : "" }
            </div>
              
        );
    }
   
    
}