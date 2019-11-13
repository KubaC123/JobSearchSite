import React from 'react';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import JobChips from './JobChips/JobChips';

const jobCardContent = (props) => {

  let firstTechStack = null;
  let secondTechStack = null

  if(props.techStacks[0]) {
    firstTechStack = props.techStacks[0];
  }

  if(props.techStacks[1]) {
    secondTechStack = props.techStacks[1];
  }

  return (
    <Box>
      <Box paddingBottom={2}>
        <Typography component="h2">
          <strong>{props.title}</strong>
        </Typography>
      </Box>
      <JobChips
        company={props.company}
        city={props.city}
        salaryMin={props.salaryMin}
        salaryMax={props.salaryMax}
        firstTechStack={firstTechStack}
        secondTechStack={secondTechStack}
      />
    </Box>
  );
}

export default jobCardContent;