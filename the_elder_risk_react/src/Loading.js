import React, { Component } from 'react';
import loadingSrc from './images/loading_eso.png';
import './Loading.css';


class Loading extends Component{
    constructor(props){
        super(props)
        this.state = {loadingText : "Loading..."};
    }

    render(){
        return(
            <div className="loading-container">
                <div><p>{this.state.loadingText}</p></div>
                <img src={loadingSrc}/> 
            </div>
        );
    }
}

export default Loading;