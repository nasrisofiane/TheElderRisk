import React from 'react';
export default class BodyMap extends React.Component{

    handleClick(e) {
        var attaque = e.target.id;
        e.preventDefault();
        
        console.log('continant selectionner est le '+attaque);
      }
    render(){
        return(     
                <div class ="carte">
                        <div class ="tout">
                        <div id  = "Cerveau"  onClick={this.handleClick}>
                        <div id ="Cerveau1">Cerveau1</div>
                        <div id ="Cerveau2" >Cerveau2</div>
                        <div id ="Cerveau3" >Cerveau3</div>

                        <div id ="Cerveau4" >Cerveau4</div>
                        <div id ="Cerveau5">Cerveau5</div>
                        <div id ="Cerveau6" >Cerveau6</div>
                        </div>




                        <div id = "Poumondroit" onClick={this.handleClick}>
                        <div id ="Poumondroit1" >Poumondroit1</div>
                        <div id="Poumondroit2" >Poumondroit2</div>
                        <div id ="Poumondroit3" >Poumondroit3</div>
                        </div>

                        <div id ="Poumongauche" onClick={this.handleClick}>
                        <div id ="Poumongauche1" >Poumongauche1</div>
                        <div id="Poumongauche2" >Poumongauche2</div>
                        <div id ="Poumongauche3" >Poumongauche3</div>
                        </div>


                        <div id ="Coeur" onClick={this.handleClick}>
                        <div id ="Coeur1" >Coeur1</div>
                        <div id ="Coeur2" >Coeur2</div>
                        </div>


                        <div id="Foie" onClick={this.handleClick}>
                        <div id="Foie1" >Foie1</div>
                        <div id ="Foie2" >Foie2</div>
                        <div id ="Foie3" >Foie3</div>
                        </div>

                        <div id ="Estomac" onClick={this.handleClick}>
                        <div id ="Estomac1" >Estomac1</div>
                        <div id="Estomac2" >Estomac2</div>
                        </div>

                        <div id="Rein" onClick={this.handleClick}>
                        <div id="Rein1" >Rein1</div>
                        <div id ="Rein2" >Rein2</div>
                        <div id="Rein3" >Rein3</div>
                        </div>




                        <div id ="Grosintestin" onClick={this.handleClick}>
                        <div id ="Grosintestin1" >Grosintestin1</div>
                        <div id ="Grosintestin2" >Grosintestin2</div>
                        <div id ="Grosintestin3" >Grosintestin3</div>
                        </div>


                        <div id ="Petitintestin" onClick={this.handleClick}>
                        <div id ="Petitintestin1" >Petitintestin1</div>
                        <div id ="Petitintestin2" >Petitintestin2</div>
                        <div id ="Petitintestin3" >Petitintestin3</div>
                        <div id ="Petitintestin4" >Petitintestin4</div>
                    </div>
                </div>
                </div>
        );
    }
   
    
}