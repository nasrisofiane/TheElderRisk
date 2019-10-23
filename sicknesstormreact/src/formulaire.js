import React from 'react';


class Formulaire extends React.Component{
    state = {
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
      }

    render(){
        return (<div className ="champs">

    <div className ="champs1">   
        <p>joueur1</p>
        <input type="text"name="name" />
        <button onClick="champ1">{this.state.data}Username</button>
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
  


export default Formulaire