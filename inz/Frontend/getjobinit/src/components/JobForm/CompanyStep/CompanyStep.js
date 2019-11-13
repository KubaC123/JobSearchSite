import React from 'react';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import TextEditor from '../TextEditor/TextEditor';

const companyStep = (props) => {

  let classes = {
    TextField: {
      width: 300
    }
  }

  return(
    <Grid container direction="column" alignContent="flex-start" spacing={6}>
      <Grid item>
        <TextField label="Name" style={classes.TextField} 
          value={props.name} onChange={props.nameChanged}/>
      </Grid>
      <Grid item>
        <TextField label="Size" style={classes.TextField} 
          value={props.size} onChange={props.sizeChanged}/>
      </Grid>
      <Grid item>
        <TextField label="Establishment" style={classes.TextField} 
          value={props.establishment} onChange={props.establishmentChanged}/>
      </Grid>
      <Grid item>
        <TextField label="Facebook" style={classes.TextField} 
          value={props.facebook} onChange={props.facebookChanged}/>
      </Grid>
      <Grid item>
        <TextField label="Linkedin" style={classes.TextField} 
          value={props.linkedin} onChange={props.linkedinChanged}/>
      </Grid>
      <Grid item>
        <TextField label="Twitter" style={classes.TextField} 
          value={props.twitter} onChange={props.twitterChanged}/>
      </Grid>
      <Grid item>
        <TextField label="Instagram" style={classes.TextField} 
        value={props.instagram} onChange={props.instagramChanged}/>
      </Grid>
      <Grid item>
        <TextEditor label="Provide company description here..." 
          textChanged={props.companyDescriptionChanged}/>
      </Grid>
    </Grid>
  )
}

export default companyStep;