import React, { Component } from 'react';
import Keycloak from 'keycloak-js';
import Box from '@material-ui/core/Box';
import Paper from '@material-ui/core/Paper';
import {stateToHTML} from 'draft-js-export-html';
import {stateFromHTML} from 'draft-js-import-html';
import { convertToRaw, convertFromHTML, ContentState } from 'draft-js';
import getJobInItClient from '../../GetJobInItClient';
import JobStepper from '../JobStepper/JobStepper';
import CompanyStep from '../../components/JobForm/CompanyStep/CompanyStep';
import WorkFormsStep from '../../components/JobForm/WorkFormsStep/WorkFormsStep';
import JobStep from '../../components/JobForm/JobStep/JobStep';

class JobForm extends Component {

  state = {
    keycloak: null,
    authenticated: false,
    job: {
      title: null,
      company: {
        name: null,
        size: null,
        establishment: null,
        description: null,
        instagramUrl: null,
        facebookUrl: null,
        linkedinUrl: null,
        twitterUrl: null
      },
      type: null,
      category: null,
      technology: null,
      experienceLevel: null,
      employmentType: null,
      salaryMin: null,
      salaryMax: null,
      startDate: null,
      description: null,
      projectIndustry: null,
      projectTeamSize: null,
      projectDescription: null,
      techStacks: [],
      locations: []
    },
    technologies: [],
    categories: []
  }

  componentDidMount() {
    this.getCategories();
    this.getTechnologies();
    const keycloak = Keycloak('/keycloak.json');
    keycloak.init({onLoad: 'login-required', promiseType: 'native'}).then(authenticated => {
      this.setState({ keycloak: keycloak, authenticated: authenticated })
    })
  }

  getCategories() {
    getJobInItClient.get("/job-service/api/category/all")
    .then(response => {
      const categories = response.data;
      this.setState({categories: categories});
    })
  }

  getTechnologies() {
    getJobInItClient.get("/job-service/api/technology/all")
    .then(response => {
      const technologies = response.data;
      this.setState({technologies: technologies});
    })
  }

  handleNameChanged = (event) => {
    let name = event.target.value;
    this.setState({job: { ...this.state.job, company: { ...this.state.job.company, name: name }}});
  }

  handleSizeChanged = (event) => {
    let size = event.target.value;
    this.setState({job: { ...this.state.job, company: { ...this.state.job.company, size: size }}});
  }

  handleEstablishmentChanged = (event) => {
    let establishment = event.target.value;
    this.setState({job: { ...this.state.job, company: { ...this.state.job.company, establishment: establishment }}});
  }

  handleFacebookChanged = (event) => {
    let facebook = event.target.value;
    this.setState({job: { ...this.state.job, company: { ...this.state.job.company, facebookUrl: facebook }}});
  }

  handleLinkedinChanged = (event) => {
    let linkedin = event.target.value;
    this.setState({job: { ...this.state.job, company: { ...this.state.job.company, linkedinUrl: linkedin }}});
  }

  handleTwitterChanged = (event) => {
    let twitter = event.target.value;
    this.setState({job: { ...this.state.job, company: { ...this.state.job.company, twitterUrl: twitter }}});
  }

  handleInstagramChanged = (event) => {
    let instagram = event.target.value;
    this.setState({job: { ...this.state.job, company: { ...this.state.job.company, instagramUrl: instagram }}});
  }

  handleCompanyDescriptionChanged = (editorState) => {
    let description = editorState.getCurrentContent();
    let htmlDesc = stateToHTML(description);
    console.log(htmlDesc);
    this.setState({job: { ...this.state.job, company: { ...this.state.job.company, description: description }}});
  }

  handleTypeChanged = (event, type) => {
    if(type) {
      let value = type.name;
      console.log(value);
      this.setState({job: { ...this.state.job, type: value }});
    }
  }

  handleEmploymentTypeChanged = (event, emplType) => {
    if(emplType) {
      let value = emplType.name;
      console.log(value);
      this.setState({job: { ...this.state.job, employmentType: value }});
    }
  }

  handleSalaryChanged = (salaryRange) => {
    if(salaryRange) {
      let salaryMin = salaryRange[0];
      let salaryMax = salaryRange[1];
      console.log(salaryMin);
      console.log(salaryMax);
      this.setState({job: { ...this.state.job, salaryMin: salaryMin, salaryMax: salaryMax }});
    }
  }

  handleTechnologyChanged = (event, technology) => {
    if(technology) {
      console.log(technology);
      this.setState({job: { ...this.state.job, technology: technology }});
    }
  }

  handleCategoryChanged = (event, category) => {
    if(category) {
      console.log(category);
      this.setState({job: { ...this.state.job, category: category }});
    }
  }

  render() {
    if (this.state.keycloak) {
      if (this.state.authenticated) return (
        <div>
          <p>Auth in progress...</p>
        </div>
      ); else return (<div>Auth in progress...</div>)
    }

    // todo fix
    // let companyDescription = null;
    // if(this.state.job.company.description) {
    //   let descriptionHTML = this.state.job.company.description;
    //   console.log(descriptionHTML);
    //   if(descriptionHTML !== "<p><br></p>") {
    //     let contentHTML = convertFromHTML(descriptionHTML);
    //     let state = ContentState.createFromBlockArray(contentHTML.contentBlocks, contentHTML.entityMap);
    //     companyDescription = JSON.stringify(convertToRaw(state));
    //   }
    // }

    let companyStep = (
      <CompanyStep
        name={this.state.job.company.name}
        nameChanged={this.handleNameChanged}
        size={this.state.job.company.size}
        sizeChanged={this.handleSizeChanged}
        establishment={this.state.job.company.establishment}
        establishmentChanged={this.handleEstablishmentChanged}
        facebook={this.state.job.company.facebookUrl}
        facebookChanged={this.handleFacebookChanged}
        linkedin={this.state.job.company.linkedinUrl}
        linkedinChanged={this.handleLinkedinChanged}
        twitter={this.state.job.company.twitterUrl}
        twitterChanged={this.handleTwitterChanged}
        instagram={this.state.job.company.instagramUrl}
        instagramChanged={this.handleInstagramChanged}
        companyDescriptionChanged={this.handleCompanyDescriptionChanged}
      />
    )

    let workFormsStep = (
      <WorkFormsStep 
        type={this.state.job.type}
        typeChanged={this.handleTypeChanged}
        employmentType={this.state.job.employmentType}
        employmentTypeChanged={this.handleEmploymentTypeChanged}
        salaryMin={this.state.job.salaryMin}
        salaryMax={this.state.job.salaryMax}
        salaryChanged={this.handleSalaryChanged}
      />
    )

    let jobStep = (
      <JobStep 
        technologies={this.state.technologies}
        technology={this.state.job.technology}
        technologyChanged={this.handleTechnologyChanged}
        category={this.state.job.category}
        categoryChanged={this.handleCategoryChanged}
        categories={this.state.categories}
      />
    )

    return (
      <Box maxWidth="70%" mx="auto" paddingTop={10}>
        <Paper>
          <JobStepper 
            firstStep={companyStep}
            secondStep={workFormsStep}
            thirdStep={jobStep}
          />
        </Paper>
      </Box>
    );
  }
}

export default JobForm;