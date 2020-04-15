import React from 'react';
import AttackPhase from './AttackPhase';
import MoveFortify from './MoveFortify';
import PlayerTurn from './PlayerTurn';
import PlacePawnInterface from './PlacePawnInterface';
import SvgMap from './SvgMap';
import './body_map.css';
import UserInformations from './UserInformations';


export default class BodyMap extends React.Component{
    
    constructor(){
        super();
        this.state = {
            diceOne : 1,
            isLoaded : true, 
            prevTerritorySelectedAttacker:null, 
            prevTerritorySelectedDefender:null, 
            territoryAttackerSelected:null, 
            territoryDefenderSelected:null, 
            switchSelect:false, 
            tooltip: null, 
            factionsDivs : [], 
            openLastFightResults : false,
            attackerTerritory : null,
            defenderTerritory : null,
            timerInterval : null,
            timer : null
        };
        this.handleEvent = this.handleEvent.bind(this);
    }

    componentDidMount = () =>{
        this.createTimer(this.props.game.endingTurnTime);
    }

    createFactionIconDivs = () =>{
        const mapContainer = document.getElementById("map-container");
        const allTerritoriesNameSvg = document.getElementById("territories_name").getElementsByTagName("text");
        const allPathsSvg = document.getElementById("map_informations").querySelectorAll('path');
        const iconsContainerSvgPaths = [];
        const divsElements = [];

        for(let i = 0; i < allPathsSvg.length; i++){
            if(allPathsSvg[i].getBBox().width > 23){
                iconsContainerSvgPaths.push(allPathsSvg[i]);
                allPathsSvg[i].style.strokeOpacity = "0";
            }
        }

        for(let i = 0; i < iconsContainerSvgPaths.length; i++){
            const iconContainerDiv = document.createElement("div");
            iconContainerDiv.style.width = `${iconsContainerSvgPaths[i].getBBox().width}px`;
            iconContainerDiv.style.height = `${iconsContainerSvgPaths[i].getBBox().height}px`;
            iconContainerDiv.style.position = "absolute";
            iconContainerDiv.style.top = `${iconsContainerSvgPaths[i].getBBox().y}px`;
            iconContainerDiv.style.left = `${iconsContainerSvgPaths[i].getBBox().x}px`;

            divsElements[i] = iconContainerDiv;
        }

        for(let i = 0; i < divsElements.length; i++){
            mapContainer.appendChild(divsElements[i]);
        }

        for(let j = 0; j < divsElements.length; j++){
            let myPosition = divsElements[j];
            let closest = allTerritoriesNameSvg[0];
            let shortestDistance = this.distanceCalculated(myPosition, allTerritoriesNameSvg[0].getBBox());
    
            for (let i = 0; i < allTerritoriesNameSvg.length; i++) {
                let d = this.distanceCalculated(myPosition, allTerritoriesNameSvg[i].getBBox());
    
                if (d < shortestDistance) {
                    closest = allTerritoriesNameSvg[i];
                    shortestDistance = d;
                }
            }

            const closestNameTerritory = closest.textContent.replace(/[^a-zA-Z0-9]/g, "");
            myPosition.classList.add(closestNameTerritory);
            this.state.factionsDivs[j] = [myPosition, closestNameTerritory];
        }
        this.findFactionPerTerritory();
    }

    findFactionPerTerritory = () =>{
        for(let i = 0; i < this.state.factionsDivs.length; i++){

            for(let j = 0 ; j < this.props.territories.length; j++){
                if(this.state.factionsDivs[i][1] === this.props.territories[j][1]){
                    this.state.factionsDivs[i][2] = this.props.territories[j][3];
                    this.state.factionsDivs[i][0].className = `FactionIconContainerDiv player_${this.props.territories[j][4]}`;
                }
            }
        }
    }

    distanceCalculated = (pt1, pt2) => {
        var diffX = pt1.offsetLeft - pt2.x;
        var diffY = pt1.offsetTop - pt2.y;
        
        return (diffX*diffX+diffY*diffY);
    }

    createPawnIconDivs = () =>{
        const mapContainer = document.getElementById("map-container");
        const allPathsSvg = document.getElementById("map_informations").querySelectorAll('path');
        const iconsContainer = [];

        for(let i = 0; i < allPathsSvg.length; i++){
            if(allPathsSvg[i].getBBox().width < 22){
                iconsContainer.push(allPathsSvg[i]);
                allPathsSvg[i].style.strokeOpacity = "0";
            }
        }
        
        for(let i = 0; i < iconsContainer.length; i++){
            const iconContainerDiv = document.createElement("div");
            iconContainerDiv.style.width = `${iconsContainer[i].getBBox().width}px`;
            iconContainerDiv.style.height = `${iconsContainer[i].getBBox().height}px`;
            iconContainerDiv.style.position = "absolute";
            iconContainerDiv.style.top = `${iconsContainer[i].getBBox().y}px`;
            iconContainerDiv.style.left = `${iconsContainer[i].getBBox().x}px`;
            iconContainerDiv.className = "PawnIconContainerDiv";

            mapContainer.appendChild(iconContainerDiv);
        }
        this.createFactionIconDivs();
            
    }

    componentWillReceiveProps(nextProps){

        if(nextProps.game.phase !== this.props.game.phase){
          this.cleanBodyFromSelectedTerritories();
        }   
        
        if(nextProps.players != this.props.players){
            
           if(this.props.userName == this.props.game.getAttacked){
                this.displayLastFightResults();
           }
           if(this.props.game.getAttacked != null){
                if(this.props.userName == this.props.game.playerTurn.name){
                    this.displayLastFightResults();
                }
           }
        }

        if(nextProps.game.lastFightTerritories != null){
            this.setState({
                attackerTerritory : this.props.territories[nextProps.game.lastFightTerritories[0] -1],
                defenderTerritory : this.props.territories[nextProps.game.lastFightTerritories[1] -1]
            })
        }

        if(nextProps.game.getAttacked != null && nextProps.game.getAttacked == this.props.userName){
            this.setState({openLastFightResults : false});
        }

        if(nextProps.game.endingTurnTime != this.props.game.endingTurnTime){
            this.createTimer(nextProps.game.endingTurnTime);
        }
    }

    cleanBodyFromSelectedTerritories = async () =>{
        if(this.state.territoryAttackerSelected != null && this.state.territoryAttackerSelected.length){
            this.state.territoryAttackerSelected[1].classList.remove("attacker");
        }
        

        if(this.state.territoryDefenderSelected != null && this.state.territoryDefenderSelected.length){
            this.state.territoryDefenderSelected[1].classList.remove("defender");
        }

        this.setState({switchSelect:false});
        
    }

    displayTerritoryInfos = (territorySelected) => {
        this.setState({tooltip:territorySelected})
    }

    territorySelected = async (territory) =>{
        let found = false;
        let territoryDom = territory;

        if((this.props.userName === this.props.game.playerTurn.name) || (this.props.game.phase == "PREPARE")){

            for(let i = 0; i < this.props.territories.length; i++){
                if(territory.id == this.props.territories[i][1]){
                    found = true;
                    
                    territory = this.props.territories[i];
                    this.displayTerritoryInfos(territory);
                    break;
                }
            }

            if(found === true){

                if(this.props.game.phase == "MOVEFORTIFY"){
                    if(territory[3] === this.props.game.playerTurn.name){

                        if(this.state.prevTerritorySelectedAttacker != null && this.state.switchSelect === false){
                            this.state.prevTerritorySelectedAttacker.classList.remove('attacker');
                        }

                        if(this.state.switchSelect === false && !territoryDom.classList.contains('defender')){
                            territoryDom.classList.add('attacker');
                            await this.setState({prevTerritorySelectedAttacker:territoryDom, territoryAttackerSelected:[territory, territoryDom], switchSelect:true});
                            return;
                        }

                        if(this.state.prevTerritorySelectedDefender != null && this.state.switchSelect === true){
                            this.state.prevTerritorySelectedDefender.classList.remove('defender');
                        }

                        if(this.state.switchSelect === true && !territoryDom.classList.contains('attacker')){
                            territoryDom.classList.add('defender');
                            await this.setState({prevTerritorySelectedDefender:territoryDom, territoryDefenderSelected:[territory, territoryDom], switchSelect:false});
                            return;
                        }

                    }
                }

                if(this.props.game.phase == "ATTACK"){

                    if(territory[3] === this.props.game.playerTurn.name){

                        if(this.state.prevTerritorySelectedAttacker != null){
                            this.state.prevTerritorySelectedAttacker.classList.remove('attacker');
                        }

                        territoryDom.classList.add('attacker');
                        await this.setState({prevTerritorySelectedAttacker:territoryDom, territoryAttackerSelected:[territory, territoryDom]});

                    }
                    else if(territory[3] != this.props.game.playerTurn.name){

                        if(this.state.prevTerritorySelectedDefender != null){
                            this.state.prevTerritorySelectedDefender.classList.remove('defender');
                        }

                        territoryDom.classList.add('defender');
                        await this.setState({prevTerritorySelectedDefender:territoryDom, territoryDefenderSelected:[territory, territoryDom]});
                    }
                    
                }
            }

            if(found == true && (this.props.game.phase == "PREPARE" || this.props.game.phase == "PLACEPAWN")){

                if((this.props.game.phase == "PREPARE" && territory[3] === this.props.userName) || (territory[3] === this.props.game.playerTurn.name && this.props.game.phase == "PLACEPAWN")){

                    if(this.state.prevTerritorySelectedAttacker != null){
                        this.state.prevTerritorySelectedAttacker.classList.remove('attacker');
                    }

                    territoryDom.classList.add('attacker');
                    await this.setState({prevTerritorySelectedAttacker:territoryDom, territoryAttackerSelected:[territory, territoryDom]});
                }
            }
        }
    }

    //method created to be passed in a child to call the fetch that retrieve the phase data.
    handleEvent = async () => {
        await this.props.sendMessage();
    }

    answerToAttack = async () =>{
        this.props.sendMessageToAnswerFight(this.state.diceOne);
    }

    dicesInputs =({target:{id, value}}) => {
        if(id =="dice-one"){
            this.setState({diceOne:value});
        }
    }

    displayLastFightResults = (fromAttackPhase = false) =>{

        if(this.state.openLastFightResults == false && fromAttackPhase == false){
            this.setState({openLastFightResults : true});
        }
        else{
            this.setState({openLastFightResults : false});
        }
        
    }

    createTimer = (endingTurnTime) =>{
        clearInterval(this.state.timerInterval);
        let dateEnding = new Date(endingTurnTime).getTime();
        
        this.setState({timer : dateEnding, timerInterval : setInterval(() =>{
            if(this.state.timer > 0){
                this.setState({timer : this.state.timer -1 });
            }
            else{
                clearInterval(this.state.timerInterval);
            }
        }, 1000)});

    }

    renderFight(){
        let resultLastFight;
        

        for(let i = 0; i < this.props.players.length; i++){
            if(this.props.userName == this.props.players[i].name){
                resultLastFight = this.props.players[i].lastFightResults;
                break;
            }
        }

        if(resultLastFight != null){
            return(
                <div className="fight-infos-window">
                    <div className="fight-results"> 
                            <div className="fight-informations">
                                <div className="table-results">
                                    <table>
                                        <thead>
                                            <tr>
                                                <th>
                                                    <div className={`player_${this.state.attackerTerritory[4]} results-flag`}></div>
                                                    <p>{this.state.attackerTerritory != null ? `${this.state.attackerTerritory[1]} - ${this.state.attackerTerritory[3]} `: "Loading"}</p>
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {resultLastFight[1].map((dices, index) => {return <tr key={index}><td><div className={`dices dice_${dices}`}></div></td></tr> })}
                                        </tbody>
                                    </table>

                                    <table>
                                        <thead>
                                            <tr>
                                                <th>
                                                    <div className={`player_${this.state.defenderTerritory[4]} results-flag`}></div>
                                                    <p>{this.state.defenderTerritory != null ? `${this.state.defenderTerritory[1]} - ${this.state.defenderTerritory[3]} ` : "Loading"}</p>
                                                    </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {resultLastFight[2].map((dices, index) => { return <tr key={index}><td><div className={`dices dice_${dices}`}></div></td></tr> })}
                                        </tbody>
                                    </table>
                                </div>
                                <div>
                                    <h2>Fight result</h2>
                                    <div>{this.state.attackerTerritory[1]} - {this.state.attackerTerritory[3]} loosed {resultLastFight[0][1]} pawn(s) </div>
                                    <div>{this.state.defenderTerritory[1]} - {this.state.defenderTerritory[3]} loosed {resultLastFight[0][0]} pawn(s) </div>
                                </div>
                                <button className="close-fight-result-button" onClick={() => this.setState({openLastFightResults : false})}>Close</button>
                            </div>
                        </div>
                </div>);
        }
        else{
            return(
                <div className="fight-infos-window">
                    <div className="fight-results"> 
                        <div className="background-fight-result-container">
                            <div className="fight-informations">
                                <div> 
                                    <p>No fight has been done</p>
                                </div>
                                <button className="close-fight-result-button" onClick={() => this.setState({openLastFightResults : false})}>Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            );
        }
        
            
    }
    
    render(){
        return(
            <div id="game-window">
                <UserInformations territories={this.props.territories} userName={this.props.userName}/>
                
                {this.props.game.getAttacked != null && this.props.game.getAttacked == this.props.userName ? 
                <div id="answer-attack">
                    <div id="answer-attack-infos-container">
                        <h2>Answer to the attack</h2>
                        <h3>Dice</h3>
                        <input id="dice-one" type="number" value={this.state.diceOne} max="3" min="1" onChange={this.dicesInputs}/>
                        <button onClick={() => this.answerToAttack()}>Defend with {this.state.diceOne} pawns</button>
                        <p><strong className="territory-infos-results-defender">{this.state.defenderTerritory[1]}</strong> get attacked by <strong className="territory-infos-results-attacker">{this.state.attackerTerritory[1]}</strong> with <strong className="attack-dice-infos">{this.props.game.lastFightTerritories[2]}</strong> dices</p>
                        <p><strong className="territory-infos-results-defender">{this.state.defenderTerritory[1]}</strong> have {this.state.defenderTerritory[2]} pawns</p>
                    </div>
                </div> : ""}
                {this.props.userName != null ? this.props.game.userName : "ERROR"}
                    
                    <section className="game-ui">
                        {this.state.openLastFightResults == true ? this.renderFight() : ""}
                        {this.state.isLoaded ? <SvgMap findFactionPerTerritory={this.findFactionPerTerritory} createPawnIconDivs={this.createPawnIconDivs} updateTerritories={this.props.territories} territorySelected={this.territorySelected}/> : "loading"}
                    </section>
                    <section id="user-ui">
                        <button className="show-fight-button" onClick={() => this.displayLastFightResults()}>Last fight results</button>
                        {this.props.game.phase != "INITIALIZE" ? <PlayerTurn userName={this.props.userName} timer={this.state.timer} game={this.props.game} playerturn={this.props.game.playerTurn.name} players={this.props.game.playerList}/> : ""}
                        {this.state.tooltip != null ? <div id="territory-infos"><p><i>Territory selected infos</i></p><h4><strong>{this.state.tooltip[1]}</strong></h4> <p>Number of pawns : {this.state.tooltip[2]}</p> <p>Owner : {this.state.tooltip[3]}</p></div> : ""}
                        {this.props.userName == this.props.game.playerTurn.name || this.props.game.phase == "PREPARE" ? 
                        <div id="phase-interface">
                            {this.props.game.phase != "INITIALIZE" && (this.props.game.phase == "PREPARE" || this.props.game.phase == "PLACEPAWN") ? <PlacePawnInterface game={this.props.game} userName={this.props.userName} sendMessageToAddPawns={this.props.sendMessageToAddPawns} updatephase={this.handleEvent} territoryAttackerSelected={this.state.territoryAttackerSelected}  /> : ""}
                            {this.props.game.phase != "INITIALIZE" && this.props.game.phase == "ATTACK" ? <AttackPhase displayLastFightResults={this.displayLastFightResults} players={this.props.players} userName={this.props.userName} game={this.props.game} sendMessageToCloseFightStep={this.props.sendMessageToCloseFightStep} getAttacked={this.props.game.getAttacked} sendMessageToFight={this.props.sendMessageToFight} territoryAttackerSelected={this.state.territoryAttackerSelected}  territoryDefenderSelected={this.state.territoryDefenderSelected}/> : ""}
                            {this.props.game.phase != "INITIALIZE" && this.props.game.phase == "MOVEFORTIFY" ? <MoveFortify sendMessageToMovePawns={this.props.sendMessageToMovePawns} sendMessageCloseMoveFortifyPhase={this.props.sendMessageCloseMoveFortifyPhase} cleanSelected={this.cleanBodyFromSelectedTerritories} territoryAttackerSelected={this.state.territoryAttackerSelected}  territoryDefenderSelected={this.state.territoryDefenderSelected}/> : ""}
                        </div> : "" }
                    </section>
            </div>
              
        );
    }
   
    
}