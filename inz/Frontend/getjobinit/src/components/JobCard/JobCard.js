import React from 'react';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import Box from '@material-ui/core/Box';
import JobCardContent from './JobCardContent/JobCardContent';
import { Link } from 'react-router-dom';

const jobCard = (props) => {

  let job = props.job;

  return(
    <Box paddingTop={2} paddingBottom={2}>
      <Box borderLeft={10} borderRadius={10} borderColor="secondary.main">
        <Link to={"/job-overview/" + job.id} style={{ textDecoration: 'none' }}>
          <Card>
            <CardActionArea>
              <CardContent>
                <Box>
                  <JobCardContent
                    title={job.title}
                    company={job.companyName}
                    city={job.cities[0]}
                    salaryMin={job.salaryMin}
                    salaryMax={job.salaryMax}
                    techStacks={job.techStacks}
                  />
                </Box>
              </CardContent>
            </CardActionArea>
          </Card>
        </Link>
      </Box>
    </Box>
  );
}

export default jobCard;