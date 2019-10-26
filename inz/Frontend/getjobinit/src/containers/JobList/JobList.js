import React from 'react';
import Box from '@material-ui/core/Box';
import Paper from '@material-ui/core/Paper';
import JobCard from './JobCard/JobCard';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';

const jobList = (props) => {

  let jobCards = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map(nr => {
    return (
      <Box pt={1} pb={1}>
        <JobCard />
      </Box>
    );
  });

  return (
    <Box maxWidth="50%" mx="auto" pt={10}>
    <Typography component="h3">
      Matching offers:
    </Typography>
      {jobCards}
    </Box>
  );
}

export default jobList;