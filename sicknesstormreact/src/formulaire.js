import React from 'react';


class Formulaire extends React.Component{
   /* state = {
        loading: true,

      };
    async postData() {

        const bodyRequest = {
            method: 'POST',
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/json',
            },
            mode: 'cors',
            cache: 'default',
            body: JSON.stringify({name: 'Christophe'})
          };

        const response = await fetch ('http://localhost:8080/addplayer', bodyRequest);
      }*/

    
          
          /*state={
              items: [],
              isLoaded :false,
          }
      
componentDidMount(){
    fetch('http://localhost:8080/players')
    .then(res => res.json())
    .then(json =>{
        this.setState({
            isLoaded:true,
            items:json,
        })
    });
}*/
async postData() {
 try {
let result = await fetch('http://localhost:8080/addplayer', {
method:'post',
headers:{
    'Accept':'application/json',
    'content-type':'application/json',
},
body:JSON.stringify({name:'sangoku+'})
});
 console.log(result)
} catch(e){
     console.log(e)
 }
} 
    render(){/*
        var{isLoaded,items} = this.state;
        if(!isLoaded){
            return<div>loading...</div>;
        }
        else {

return(<div className = "load">
{
    <ul>
        {items.map(item => (<li key = {item.id}>{item.name}</li>))}
    </ul>
}
</div>)}*/
       
       
        return (
        
        
        
        <div className ="champs">

    <div className ="champs1">   
        <p>joueur1</p>
        <input type="text"name="name" />
        <button onClick={()=> this.postData()}>Username</button>
    </div>
                
    <div className ="champs2">
        <p>joueur2</p>
        <input type="text"name="name" />
        <button onClick="champ1">Username</button>
    </div>

    <div className ="champs3">
        <p>joueur3</p>
        <input type="text"name="name" />
        <button onClick="champ1">Username</button>
    </div>

    <div className ="champs4">
        <p>joueur4</p>
        <input type="text"name="name" />
        <button onClick="champ1">Username</button>
    </div>
              
              

        </div>
        )



    }




}
  


export default Formulaire;