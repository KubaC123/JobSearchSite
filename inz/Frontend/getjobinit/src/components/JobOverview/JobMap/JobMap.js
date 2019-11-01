import React, { Component } from 'react';
import Box from '@material-ui/core/Box';
import { Map as LeafletMap, TileLayer, Marker, Popup } from 'react-leaflet';

class JobMap extends Component {
  
  render() {
    let markers = null;
    let centerLat = null;
    let centerLong = null;
    let coords = this.props.jobCoordinates;
    centerLat = coords[0].latitude;
    centerLong = coords[0].longitude;
    markers = coords.map(coord => {
      return (
        <Marker position={[coord.latitude, coord.longitude]} key={coord.latitude}/>
      )
    })

    return (
      <Box border={1} borderColor="secondary.main">
      <LeafletMap
        center={[centerLat, centerLong]}
        zoom={3}
        maxZoom={20}
        attributionControl={true}
        zoomControl={true}
        doubleClickZoom={true}
        scrollWheelZoom={true}
        dragging={true}
        animate={true}
        easeLinearity={0.35}
      >
        <TileLayer
          url='http://{s}.tile.osm.org/{z}/{x}/{y}.png'
          attribution="&copy; <a href=&quot;http://osm.org/copyright&quot;>OpenStreetMap</a> contributors"
        />
        {/* <Marker position={[51.1000000, 17.0333300]}>
          <Popup>
            Popup for any custom information.
          </Popup>
        </Marker> */}
        {markers}
      </LeafletMap>
      </Box>
    );
  }
}

export default JobMap;