import React from 'react';
import AttackPhase from './AttackPhase';

class PlacePawnInterface extends React.Component{ 

state={
pawn : null,
territoryAttacker:null
}
setPawn = (nbPawn , listName)=>{
    if(listName == "attackers"){
        this.setState({pawn: nbPawn.value});
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
    async addpawn() {
        try {
       let result = await fetch(`http://localhost:8080/addplayer/${this.territoriesInputs}/{pawn}`, {
       method:'get',
       headers:{
           'Accept':'application/json',
           'content-type':'application/json',
       },
       body:JSON.stringify({pawn:this.state.fullname})
       });
        console.log(result)
       } catch(e){
            console.log(e)
        }
       } 


       handleSubmit=(event)=>{ 
        event.preventDefault()
        const data = this.state
        console.log(data)
    }
    handleInputChange=(event) => {
    event.preventDefault()
    console.log(event)
    console.log(event.target.pawn)
    console.log(event.target.value)
    this.setState({
    [event.target.pawn]:event.target.value
    })
    }

render (){
<AttackPhase/>

    return(

        <div className="champs">

   <h3>how many pawns you wanna send ?</h3>
    <form onSubmit={this.handleSubmit}>
    <p><input type ="text" placeholder ="pawn" value={this.state.pawn} pawn= "fullname" onChange = {this.handleInputChange}/></p>
    <p><button onClick={()=>this.addpawn()}>Send</button></p>
    </form> 
        </div> 
        )


        
    }
}



  
export default PlacePawnInterface;
 
