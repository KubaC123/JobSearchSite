import React from 'react';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';

const autoCompleteField = (props) => {

  let currentValue = null;
  if(props.currentValue) {
    currentValue = props.options.find( ({ id }) => id === props.currentValue.id);
    console.log(currentValue);
  }

  console.log(props.options);

  return (
    <div>
      <Autocomplete
        options={props.options}
        getOptionLabel={option => option.name}
        onChange={props.fieldChanged}
        value={currentValue}
        style={{ width: 300 }}
        renderInput={params => (
          <TextField {...params} label={props.fieldName} fullWidth/>
        )}
      />
    </div>
  );
}

export default autoCompleteField;