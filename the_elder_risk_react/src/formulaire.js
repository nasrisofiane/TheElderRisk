import React from 'react';
import './Formulaire.css';

class Formulaire extends React.Component{

    constructor(props){
        super(props)
    }
   
    state = { 
        cssClass : "champs",
        players:null,
        numberOfPlayers:null,
        userName:null,
        isSent:false
    }

    postData = async (value) => {
        let valueRegexed = value.replace(/[^a-zA-Z0-9]/g, '');
        await this.props.sendMessage(valueRegexed);
        this.setState({isSent:true});
    } 

    handleInputChange = async (e) => {
        this.setState({userName : e.target.value, nbPlayersReady : 0});
    }

    componentWillReceiveProps(nextProps){
        if(nextProps.players != this.props.players){
            let nbPlayersReady = 0;
            for(let i = 0; i < nextProps.players.length;i++){
                if(nextProps.players[i].readyToPlay || this.props.players.readyToPlay){
                    nbPlayersReady+=1;
                }
            }
            this.setState({nbPlayersReady : nbPlayersReady});
        }
        this.setState({players: nextProps.players, numberOfPlayers: nextProps.players.length});
    }
    
    async getData() {
        if(this.state.numberOfPlayers >= 2 && this.state.numberOfPlayers <= 3){
            this.props.sendMessageInitiliazePhase();
        }
        else{
            alert("There's "+this.state.numberOfPlayers+" players the game cannot start");
        }
    } 

    startGameUi = () =>{
        if(this.state.players != null && this.state.nbPlayersReady > 0 && this.state.isSent === true){
            if(this.state.userName == this.state.players[0].name){
                return(<button className="start-game" onClick={()=> { this.getData()}}>Start game with {this.state.nbPlayersReady} players</button>);
            }
            else{
                return(<button disabled className="start-game">Game will starts with {this.state.nbPlayersReady} players</button>);
            }
        }
    }

    render(){ 
        return ( 
            <div className="formulaire-container">
                <div className ={this.state.cssClass}>
                    <div className ="border">
                        <div className="players">    
                        { this.state.isSent != true ? <div className ="ja">
                            <h3>Player's name'</h3>
                            <div>
                                <p><input type ="text" placeholder ="your name " value={this.state.username} name = "fullName" onChange = {this.handleInputChange}/></p>
                                <p><button onClick={()=>{this.postData(this.state.userName)}}>Let's go !</button></p>
                            </div>
                        </div> : ""}
                        <div className="initialize">
                            {this.startGameUi()}
                        </div>
                        <div id="player-joined">
                            {this.state.isSent === true ? <p>Players ready to play</p> : ""}
                            {this.state.players != null ? this.state.players.map((player)=>{return player.name != null ? <p key={player.id}>{player.name}</p> : ""}) : ""}
                        </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
  
export default Formulaire;