import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Person from './person';
import Address from './address';
import Division from './division';
import District from './district';
import Upazila from './upazila';
import RMO from './rmo';
import CityCorpPoura from './city-corp-poura';
import Union from './union';
import Ward from './ward';
import PostOffice from './post-office';
import Country from './country';
import Document from './document';
import DocumentType from './document-type';
import Agency from './agency';
import AgencyType from './agency-type';
import Assignment from './assignment';
import Designation from './designation';
import PassType from './pass-type';
import Pass from './pass';
import Zone from './zone';
import Gate from './gate';
import Lane from './lane';
import AccessProfile from './access-profile';
import Vehicle from './vehicle';
import VehicleAssignment from './vehicle-assignment';
import VehicleType from './vehicle-type';
import EntryLog from './entry-log';
import AgencyLicense from './agency-license';
import Department from './department';
import AgencyLicenseType from './agency-license-type';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="person/*" element={<Person />} />
        <Route path="address/*" element={<Address />} />
        <Route path="division/*" element={<Division />} />
        <Route path="district/*" element={<District />} />
        <Route path="upazila/*" element={<Upazila />} />
        <Route path="rmo/*" element={<RMO />} />
        <Route path="city-corp-poura/*" element={<CityCorpPoura />} />
        <Route path="union/*" element={<Union />} />
        <Route path="ward/*" element={<Ward />} />
        <Route path="post-office/*" element={<PostOffice />} />
        <Route path="country/*" element={<Country />} />
        <Route path="document/*" element={<Document />} />
        <Route path="document-type/*" element={<DocumentType />} />
        <Route path="agency/*" element={<Agency />} />
        <Route path="agency-type/*" element={<AgencyType />} />
        <Route path="assignment/*" element={<Assignment />} />
        <Route path="designation/*" element={<Designation />} />
        <Route path="pass-type/*" element={<PassType />} />
        <Route path="pass/*" element={<Pass />} />
        <Route path="zone/*" element={<Zone />} />
        <Route path="gate/*" element={<Gate />} />
        <Route path="lane/*" element={<Lane />} />
        <Route path="access-profile/*" element={<AccessProfile />} />
        <Route path="vehicle/*" element={<Vehicle />} />
        <Route path="vehicle-assignment/*" element={<VehicleAssignment />} />
        <Route path="vehicle-type/*" element={<VehicleType />} />
        <Route path="entry-log/*" element={<EntryLog />} />
        <Route path="agency-license/*" element={<AgencyLicense />} />
        <Route path="department/*" element={<Department />} />
        <Route path="agency-license-type/*" element={<AgencyLicenseType />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
