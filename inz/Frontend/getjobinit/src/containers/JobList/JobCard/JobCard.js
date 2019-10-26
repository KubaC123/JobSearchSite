import React from 'react';
import Card from '@material-ui/core/Card';
import Box from '@material-ui/core/Box';
import Chip from '@material-ui/core/Chip';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import Divider from '@material-ui/core/Divider';
import BusinessIcon from '@material-ui/icons/Business';
import LocationOnIcon from '@material-ui/icons/LocationOn';
import BriefInfo from './BriefInfo/BriefInfo';
import TechStacks from './TechStacks/TechStacks';
import CompanyLogo from './CompanyLogo/CompanyLogo';
import PaymentRemote from './PaymentRemote/PaymentRemote';

const jobCard = (props) => {

  let job = {
    techStacks: [
      {
        name: 'Java'
      },
      {
        name: 'AWS'
      },
      {
        name: 'SQL'
      },
      {
        name: 'Python'
      },
      {
        name: 'React'
      },
      {
        name: 'Java'
      },
      {
        name: 'AWS'
      },
      {
        name: 'SQL'
      },
      {
        name: 'Python'
      }
    ]
  }

  return(
    <Card>
      <CardActionArea>
        <Grid container spacing={4}>
          <CompanyLogo
            imageUrl="https://www.brandchannel.com/wp-content/uploads/2016/05/instagram-new-logo-may-2016.jpg"
          />
          <BriefInfo
            title="Java cloud developer"
            company="Netflix sp z.o.o"
            location="Wrocław Legnicka 32a"
          />
          <TechStacks
            techStacks={job.techStacks}
          />
          {/* <PaymentRemote
            salaryMin="3500"
            salaryMax="5000"
            remote="true"
          /> */}
        </Grid>
      </CardActionArea>
    </Card>
  );
}

export default jobCard;