import React from 'react';
import bacteria from './images/bacteria.png';

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

    componentDidUpdate = async () => {
        if(this.props.players != null){
            await this.handleChange(); 
        }
    }

    postData = async (value) => {
        let valueRegexed = value.replace(/[^a-zA-Z0-9]/g, '');
        await this.props.sendMessage(valueRegexed);
        this.setState({isSent:true});
    } 

    handleChange = async () =>{
        this.setState({players:this.props.players, numberOfPlayers:this.props.players.length});
    }

    handleInputChange = async (e) => {
        this.setState({userName : e.target.value});
    }
    
    async getData() {
        if(this.state.numberOfPlayers >= 2 && this.state.numberOfPlayers <= 4){
            this.props.sendMessageInitiliazePhase();
        }
        else{
            alert("There's "+this.state.numberOfPlayers+" players the game cannot start");
        }
    } 

    render(){ 
        return ( 
            <div className ={this.state.cssClass}>
                <div className ="border">
                    <div className="players">    
                    { this.state.isSent != true ? <div className ="ja">
                        <h3>Player's name'</h3>
                        <div>
                            <p><input type ="text" placeholder ="your name " value={this.state.username} name = "fullName" onChange = {this.handleInputChange}/></p>
                            <p><button onClick={()=>{this.postData(this.state.userName)}}>Send</button></p>
                        </div>
                    </div> : ""}
                    <div className="initialize">
                        {this.state.players != null && this.state.players.length > 1 ? <button onClick={()=> { this.getData()}}>Start game with ({this.state.numberOfPlayers}) players</button> : "" }
                    </div>
                    <div id="player-joined">
                        {this.state.players != null ? this.state.players.map((players)=>{return <p>{players.name}</p>}) : "Enter an username"}
                    </div>
                    </div>
                </div>
            </div>
        )
    }
}
  
export default Formulaire;