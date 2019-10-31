import React from 'react';
import Box from '@material-ui/core/Box';
import Chip from '@material-ui/core/Chip';
import LocalAtmIcon from '@material-ui/icons/LocalAtm';
import LocationOnIcon from '@material-ui/icons/LocationOn';
import BusinessIcon from '@material-ui/icons/Business';

const jobChips = (props) => {

  const chipStyle = {
    paddingRight: 2
  }

  let companyChip = null;
  if(props.company) {
    companyChip = (
      <Box style={chipStyle}>
        <Chip 
        icon={<BusinessIcon />}
        label={props.company} />
      </Box>
    )
  }

  let cityChip = null;
  if(props.city) {
    cityChip = (
      <Box style={chipStyle}>
        <Chip 
        icon={<LocationOnIcon />}
        label={props.city} />
      </Box>
    )
  }

  let paymentChip = null;
  if(props.salaryMin && props.salaryMax) {
    let label = props.salaryMin + "-" + props.salaryMax;
    paymentChip = (
      <Box style={chipStyle}>
        <Chip 
          icon={<LocalAtmIcon />}
          label={label}
        />
      </Box>
    )
  }

  let firstTechStackChip = null;
  if(props.firstTechStack) {
    firstTechStackChip = (
      <Box style={chipStyle}>
        <Chip label={props.firstTechStack} />
      </Box>
    )
  }

  let secondTechStackChip = null;
  if(props.secondTechStack) {
    secondTechStackChip = (
      <Box style={chipStyle}>
        <Chip label={props.secondTechStack} />
      </Box>
    )
  }

  return(
    <Box display="flex" flexDirection="row">
      {companyChip}
      {cityChip}
      {paymentChip}
      {firstTechStackChip}
      {secondTechStackChip}
    </Box>
  );
}

export default jobChips;