import React, { Component } from 'react';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import StepContent from '@material-ui/core/StepContent';
import Button from '@material-ui/core/Button';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import IconButton from '@material-ui/core/IconButton';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';

class JobStepper extends Component {
  
  state = {
    activeStep: 0
  }

  getStepContent(step) {
    switch (step) {
      case 0:
        return this.props.firstStep;
      case 1:
        return this.props.secondStep;
      case 2:
        return this.props.thirdStep;
        case 3:
        return (<p>to do</p>)
      default:
        return 'Unknown step';
    }
  }

  handleNext = () => {
    console.log(this.state);
    const currentStep = this.state.activeStep;
    this.setState({activeStep: currentStep + 1});
  };

  handleBack = () => {
    const currentStep = this.state.activeStep;
    this.setState({activeStep: currentStep - 1});
  };

  handleReset = () => {
    this.setState({activeStep: 0});
  };

  render() {

    let steps = ["Company", "Work forms", "Job Info", "Submit"];

return (
    <div>
      <Stepper activeStep={this.state.activeStep} orientation="vertical">
        {steps.map((label, index) => (
          <Step key={label}>
            <StepLabel>{label}</StepLabel>
            <StepContent>
              <Typography>{this.getStepContent(index)}</Typography>
              <Grid container direction="row" alignContent="flex-start" spacing={4}>
                <Grid item>
                  <IconButton disabled={this.state.activeStep === 0} onClick={this.handleBack}>
                    <ChevronLeftIcon />
                  </IconButton>
                </Grid>
                <Grid item>
                  <IconButton variant="contained" color="primary" onClick={this.handleNext}>
                    {this.state.activeStep === steps.length - 1 ? 'Finish' : (<ChevronRightIcon />)}
                  </IconButton>
                </Grid>
              </Grid>
            </StepContent>
          </Step>
        ))}
      </Stepper>
      {this.state.activeStep === steps.length && (
        <Paper square elevation={0}>
          <Typography>All steps completed - you&apos;re finished</Typography>
          <Button onClick={this.handleReset}>
            Reset
          </Button>
        </Paper>
      )}
    </div>
)
  }
}

export default JobStepper;