import React from 'react';
import './user_informations.css';

export default class UserInformations extends React.Component{

    constructor(){
        super();
        this.state = {nbOfTerritories : 0, nbOfPawnsOnBoard:0};
    }

    componentDidMount = () =>{
        this.countOwnedTerritories(this.props.territories);
        this.countAllPawns(this.props.territories);
    }

    componentWillReceiveProps = (nextProps) =>{
        if(nextProps.territories != this.props.territories ){
            this.countOwnedTerritories(nextProps.territories);
            this.countAllPawns(nextProps.territories);
        }
    }

    countOwnedTerritories = async (territories) =>{
        let count = 0;

        for(let i = 0; i < territories.length; i++){
            
            if(this.props.userName == territories[i][3]){
               count += 1;
            }
        }

        await this.setState({nbOfTerritories: count});
    }

    countAllPawns = async (territories) =>{
        let count = 0;

        for(let i = 0; i < territories.length; i++){
            if(this.props.userName == territories[i][3]){
               count += territories[i][2];
            }
        }

        await this.setState({nbOfPawnsOnBoard: count});
    }

    render(){
        return(
            <div className="player-informations">
               <div><p>{`I'm ${this.props.userName}`}</p></div>
               <div><p>{`I own ${this.state.nbOfTerritories} territories`}</p></div>
               <div><p>{`I have ${this.state.nbOfPawnsOnBoard} pawns on the board`}</p></div>
            </div>
        );
    }
}