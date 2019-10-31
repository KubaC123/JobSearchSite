import React, { Component } from 'react';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import getJobInItClient from '../../GetJobInItClient';
import JobCard from '../../components/JobCard/JobCard';
import SearchBar from '../SearchBar/SearchBar';

class JobBoard extends Component {

  state = {
    jobs: []
  }
  
  componentDidMount() {
    getJobInItClient.get("/job-service/api/job/all")
    .then(response => {
      const jobs = response.data;
      this.setState({jobs: jobs});
    })
    .catch(error => {
      alert("Server is down :(")
    })
  }

  render() {
    console.log(this.state.jobs);
    let cards = this.state.jobs.map(job => {
      return (
        <JobCard job={job}/>
      );
    });

    return (
      <Box maxWidth="70%" mx="auto">
        <SearchBar 
          
        />
        <Box paddingTop={10}>
          <Typography component="h3">
            Matching offers:
          </Typography>
        </Box>
          {cards}
      </Box>
    );
  }
};

export default JobBoard;