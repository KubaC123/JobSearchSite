import React from 'react';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import TextEditor from '../TextEditor/TextEditor';
import AutosuggestField from '../AutosuggestField/AutoSuggestField';

const jobStep = (props) => {

  let classes = {
    TextField: {
      width: 300
    }
  }

  return(
    <Grid container direction="column" alignContent="flex-start" spacing={6}>
      <Grid item>
        <TextField label="Title" style={classes.TextField} 
          value={props.name} onChange={props.nameChanged}/>
      </Grid>
      <Grid item>
        <AutosuggestField fieldName="Profile" currentValue={props.category}
          fieldChanged={props.categoryChanged} options={props.categories}/>
      </Grid>
      <Grid item>
        <AutosuggestField fieldName="Main technology" currentValue={props.technology}
          fieldChanged={props.technologyChanged} options={props.technologies}/>
      </Grid>
      <Grid item>
        <TextEditor label="Provide job description here..." textChanged={props.companyDescriptionChanged}/>
      </Grid>
    </Grid>
  )
}

export default jobStep;