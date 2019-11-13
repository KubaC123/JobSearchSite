import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import JobBoard from '../JobBoard/JobBoard';
import JobOverview from '../JobOverview/JobOverview';
import JobForm from '../JobForm/JobForm';

class MainScreen extends Component {

  render() {
    return (
      <div>
        <Route path="/" exact component={JobBoard}/>
        <Route path="/job-overview/:id" exact component={JobOverview}/>
        <Route path="/post-a-job" exact component={JobForm}/>
      </div>
    );
  }
}

export default MainScreen;