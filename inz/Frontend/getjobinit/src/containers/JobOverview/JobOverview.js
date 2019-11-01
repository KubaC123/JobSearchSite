import React, { Component } from 'react';
import Box from '@material-ui/core/Box';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import getJobInItClient from '../../GetJobInItClient';
import CompanyData from '../../components/JobOverview/CompanyData/CompanyData';
import JobMap from '../../components/JobOverview/JobMap/JobMap';
import TechStacks from '../../components/JobOverview/TechStacks/TechStacks';
import JobDescription from '../../components/JobOverview/JobDescription/JobDescription';
import ProjectDescription from '../../components/JobOverview/ProjectDescription/ProjectDescription';

class JobOverview extends Component {

  state = {
    job: null
  }

  componentDidMount() {
    console.log(this.props.match.params.id);
    let jobId = this.props.match.params.id;
    getJobInItClient.get("/job-service/api/job/" + jobId)
    .then(response => {
      this.setState({job: response.data[0]});
    })
    .catch(error => {
      alert("Server is down :(")
    })
  }

  render() {
    let companyData = null;
    let jobMap = null;
    let techStacks = null;
    let jobDescription = null;
    let projectDescription = null;
    if(this.state.job) {
      companyData = (
        <CompanyData 
          company={this.state.job.company} 
          title={this.state.job.title}
          salaryMin={this.state.job.salaryMin}
          salaryMax={this.state.job.salaryMax}
          technology={this.state.job.technology.name}
          category={this.state.job.category.name}
        />
      )

      let jobCoordinates = null;
      jobCoordinates = this.state.job.locations.map(loc => {
        return {
          latitude: loc.location.latitude,
          longitude: loc.location.longitude
        }
      })
      jobMap = (
        <JobMap jobCoordinates={jobCoordinates}/>
      )

      techStacks = (
        <TechStacks techStacks={this.state.job.techStacks} />
      )

      jobDescription = (
        <JobDescription description={this.state.job.description} />
      )

      projectDescription = (
        <ProjectDescription 
          description={this.state.job.projectDescription} 
          teamSize={this.state.job.projectTeamSize}
          industry={this.state.job.projectIndustry}
        />
      )
    }

    return (
      <Box maxWidth="70%" mx="auto" paddingTop={10} border={2}>
        <Paper>
        <Grid container direction="row" justify="center" spacing={8}>
          <Grid item>
            {companyData}
          </Grid>
          <Grid item>
            {jobMap}  
          </Grid>
        </Grid>
        <Grid container direction="column" justify="center" spacing={8}>
          <Grid item>
            {techStacks}
          </Grid>
          <Grid item>
            {jobDescription}
          </Grid>
          <Grid item>
            {projectDescription}
          </Grid>
        </Grid>
        </Paper>
      </Box>
    );
  }
}

export default JobOverview;