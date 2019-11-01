import React, { Component } from 'react';
import JobBoard from '../JobBoard/JobBoard';
import JobOverview from '../JobOverview/JobOverview';
import { Route } from 'react-router-dom';

class MainScreen extends Component {

  render() {
    return (
      <div>
        <Route path="/home" exact render={() => <h1>HOME</h1>}/>
        {/* <JobBoard /> */}
        <Route path="/" exact component={JobBoard}/>
        <Route path="/job-overview/:id" exact component={JobOverview}/>
      </div>
    );
  }
}

export default MainScreen;