import React from 'react';


class Formulaire extends React.Component{

    constructor(props){
        super(props)
    }
    dismiss() {
        this.props.unmountMe();
    } 

    
//permet de lancer une requete 'post' et entrer une valeur dans la base de données . 
async postData(value) {
    try {
        let result = await fetch('http://localhost:8080/addplayer', {
        method:'post',
        headers:{
            'Accept':'application/json',
            'content-type':'application/json',
        },
        
        body:JSON.stringify({name:value})
        });
        console.log(result)
    } catch(e){
        console.log(e)
    }
} 

/////////////////////////////////////////////////////////////////////////////////


//permet d'entrer un nom et l'ajouter dans la base de données en intéragissant avec la requête 'post' 

state = { 
    fullName:null,
    cssClass : "champs"
}
handleChange = event=>{
    console.log(event.target.value)
}
handleSubmit=(event)=>{ 
    event.preventDefault()
    const data = this.state
    console.log(data)
}
handleInputChange=(event) => {
event.preventDefault()
console.log(event)
console.log(event.target.name)
console.log(event.target.value)
this.setState({
[event.target.name]:event.target.value
})
}
/////////////////////////////////////////////////////////////



//permet de lancer une requete 'get' qui permettra d'initialiser une partie en prenant en compte la requete 'post' 
 async getData() {
     try {
   let result = await fetch('http://localhost:8080/initializegame') 
   let data = await result.json()
    await this.props.updatephase();
   console.log(data);
     }
 catch(e){
        console.log(e)
    }
   } 

////////////////////////////////////////////////////////////////////////////////////////////////////


    render(){ 
       
        return ( 
     
        <div className ={this.state.cssClass}>
<div className ="border">


<div className="players">    

<div className ="ja">
    <h3>Player 1</h3>
    <form onSubmit={this.handleSubmit}>
    <p><input type ="text" placeholder ="your name " value={this.state.fullName} name = "fullname" onChange = {this.handleInputChange}/></p>
    <p><button onClick={()=>this.postData(this.state.fullName)}>Send</button></p>
    </form> 
</div>


<div className ="jb">
    <h3>Player 2</h3>
    <form onSubmit={this.handleSubmit}>
    <p><input type ="text" placeholder ="your name " value={this.state.fullName1} name = "fullname1" onChange = {this.handleInputChange}/></p>
    <p><button onClick={()=>this.postData(this.state.fullName1)}>Send</button></p>
    </form> 
</div>


<div className ="jc">
    <h3>Player 3</h3>
    <form onSubmit={this.handleSubmit}>
    <p><input type ="text" placeholder ="your name " value={this.state.fullName2} name = "fullname2" onChange = {this.handleInputChange}/></p>
    <p><button onClick={()=>this.postData(this.state.fullName2)}>Send</button></p>
    </form> 
</div>


<div className ="jd">
    <h3>Player 4</h3>
    <form onSubmit={this.handleSubmit}>
    <p><input type ="text" placeholder ="your name " value={this.state.fullName3} name = "fullname3" onChange = {this.handleInputChange}/></p>
    <p><button onClick={()=>this.postData(this.state.fullName3)}>Send</button></p>
    </form> 
</div>



<div className="initialize">
    <button onClick={()=> { this.getData()}}>initialize</button>
</div>


</div>



</div>
  

        </div>

        )
    }
}
  
export default Formulaire;