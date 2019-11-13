import React, { Component } from 'react';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Slider from '@material-ui/core/Slider';
import AutosuggestField from '../AutosuggestField/AutoSuggestField';

class WorkFormsStep extends Component {

  state = {
    range: [10, 20]
  }

  salaryRangeChanged = (event, newValue) => {
    console.log(newValue);
    this.props.salaryChanged([newValue[0]*1000, newValue[1]*1000]);
    this.setState({range: newValue});
  }

  render() {
    let classes = {
      TextField: {
        width: 300
      }
    }

    let jobTypes = [
      { id: 1, name: 'Full time' },
      { id: 2, name: 'Part time' },
      { id: 3, name: 'Internship' },
      { id: 4, name: 'Consulting' },
    ]

    let employmentTypes = [
      { id: 1, name: 'B2B' },
      { id: 2, name: 'Employment' },
      { id: 3, name: 'Contract agreement' },
      { id: 4, name: 'Contract for specific work' },
      { id: 5, name: 'Intern agreement' },
    ]

    let currentType = this.props.type;
    if(currentType) {
      currentType = jobTypes.find( ({ name }) => name === currentType);
    }

    let currentEmploymentType = this.props.employmentType;
    if(currentEmploymentType) {
      currentEmploymentType = employmentTypes.find( ({ name }) => name === currentEmploymentType);
    }

    return(
      <Grid container direction="column" alignContent="flex-start" spacing={6}>
        <Grid item>
          <AutosuggestField fieldName="Type" currentValue={currentType} 
            fieldChanged={this.props.typeChanged} options={jobTypes}/>
        </Grid>
        <Grid item>
          <AutosuggestField fieldName="Employment type" currentValue={currentEmploymentType}
            fieldChanged={this.props.employmentTypeChanged} options={employmentTypes}/>
        </Grid>
        <Grid item>
          <Typography id="range-slider" gutterBottom>
            Salary range
          </Typography>
          <Slider
            onChange={this.salaryRangeChanged}
            value={this.state.range}
            min={0} max={30}
            step={1} marks
            aria-labelledby="range-slider"
            valueLabelDisplay="auto"
            valueLabelFormat={x => x + "k"}
          />
        </Grid>
      </Grid>
    )
  } 
}

export default WorkFormsStep;