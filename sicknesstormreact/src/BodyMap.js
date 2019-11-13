import React from 'react';
import AttackPhase from './AttackPhase';
import MoovFortify from './MoovFortify';
import PlayerTurn from './PlayerTurn';
import PlacePawnInterface from './PlacePawnInterface';
import SvgBody from './SvgBody';


export default class BodyMap extends React.Component{
    
    constructor(){
            super();
            this.state = {diceOne : 1, isLoaded : false, game:null, getAttacked:null,territories:{}, roundPhase:null, playerTurn : null, prevTerritorySelectedAttacker:null, prevTerritorySelectedDefender:null, territoryAttackerSelected:null, territoryDefenderSelected:null, switchSelect:false, tooltip: null, styles: { top: 0, left: 0}};
            this.handleEvent = this.handleEvent.bind(this);
    }
        
    async componentDidMount(){
        this.props.sendMessage();
    }

    componentDidUpdate = async () =>{
        let dataTreated = this.props.territories.map((territories) => { return [territories.id, territories.name.toLowerCase(), territories.pawn, territories.player.name, territories.player.id]});
            this.setState({
                isLoaded : true,
            territories : dataTreated.sort(function(a, b) {return a[0] - b[0];})
        })
        this.setState({game:this.props.game, getAttacked : this.props.game.getAttacked ,roundPhase: this.props.game.phase, playerTurn:this.props.game.playerTurn.name});
        if(this.state.getAttacked != null){
            if(this.state.getAttacked == this.props.userName){
                console.log("ATTACK RECEIVED !");
            }
            else{
                console.log("NOT ATTACKED");
            }
        }
    }

    cleanBodyFromSelectedTerritories = async () =>{
        this.state.territoryAttackerSelected[1].classList.remove("attacker");
        this.state.territoryDefenderSelected[1].classList.remove("defender");
    }

    eventOnMouseMove = (e) => {
        this.setState({styles:{top:e.screenY-90+"px", left:e.screenX-70+"px"}});
    }

    territorySelected = async (territory) =>{
        let found = false;
        let territoryDom = territory;
        for(let i = 0; i < this.state.territories.length; i++){
            if(territory.id == this.state.territories[i][1]){
                found = true;
                
                territory = this.state.territories[i];
                break;
            }
        }
        if(found == true && (this.state.switchSelect == false && (this.state.roundPhase == "ATTACK" || this.state.roundPhase == "MOVEFORTIFY") ) || (found == true && this.state.roundPhase == "PLACEPAWN") ){
            if(this.state.prevTerritorySelectedAttacker != null){
                
                this.state.prevTerritorySelectedAttacker.classList.remove('attacker');
            }
            territoryDom.classList.add('attacker');
            await this.setState({prevTerritorySelectedAttacker:territoryDom, territoryAttackerSelected:[territory, territoryDom]});
            if(this.state.switchSelect == false && (this.state.roundPhase == "ATTACK" || this.state.roundPhase == "MOVEFORTIFY")){
                this.setState({switchSelect:true});
            }
        }
        else if(found == true && this.state.switchSelect == true && (this.state.roundPhase == "ATTACK" || this.state.roundPhase == "MOVEFORTIFY")){
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
                {this.state.getAttacked != null && this.state.getAttacked == this.props.userName ? 
                <div id="answer-attack">
                    <h2>Answer to the attack</h2>
                    <h3>Dice</h3>
                    <input id="dice-one" type="number" value={this.state.diceOne} max="3" min="1" onChange={this.dicesInputs}/>
                <button onClick={() => this.answerToAttack()}>Defend with {this.state.diceOne} pawns</button>GET ATTACKED
                    </div> : ""}
                {this.props.userName != null ? this.props.userName : "ERROR"}
                
                    {this.state.tooltip != null ? this.state.tooltip : ""}
                    {this.state.isLoaded ? <SvgBody  updateTerritories={this.state.territories} territorySelected={this.territorySelected} territorySelectedOnMouseOver={this.territorySelectedOnMouseOver} territoryTooltipCleanUpOnMouseOut={this.territorySelectedOnMouseOut} ></SvgBody> : "Loading..."}
                    {this.state.roundPhase != "INITIALIZE" ? <PlayerTurn playerturn={this.state.playerTurn} players={this.props.players}/> : ""}
                    {this.props.userName == this.state.playerTurn ? 
                    <div id="phase-interface">
                        {this.state.roundPhase != "INITIALIZE" && this.state.roundPhase == "PLACEPAWN" ? <PlacePawnInterface sendMessageToAddPawns={this.props.sendMessageToAddPawns} updatephase={this.handleEvent} territoryAttackerSelected={this.state.territoryAttackerSelected}  /> : ""}
                        {this.state.roundPhase != "INITIALIZE" && this.state.roundPhase == "ATTACK" ? <AttackPhase getAttacked={this.props.game.getAttacked} sendMessageToFight={this.props.sendMessageToFight} updatephase={this.handleEvent} territoryAttackerSelected={this.state.territoryAttackerSelected}  territoryDefenderSelected={this.state.territoryDefenderSelected}/> : ""}
                        {this.state.roundPhase != "INITIALIZE" && this.state.roundPhase == "MOVEFORTIFY" ? <MoovFortify sendMessageCloseMoveFortifyPhase={this.props.sendMessageCloseMoveFortifyPhase} updatephase={this.handleEvent} cleanSelected={this.cleanBodyFromSelectedTerritories} territoryAttackerSelected={this.state.territoryAttackerSelected}  territoryDefenderSelected={this.state.territoryDefenderSelected}/> : ""}
                    </div> : "" }
            </div>
              
        );
    }
   
    
}