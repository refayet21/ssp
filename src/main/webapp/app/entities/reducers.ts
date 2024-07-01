import person from 'app/entities/person/person.reducer';
import address from 'app/entities/address/address.reducer';
import division from 'app/entities/division/division.reducer';
import district from 'app/entities/district/district.reducer';
import upazila from 'app/entities/upazila/upazila.reducer';
import rMO from 'app/entities/rmo/rmo.reducer';
import cityCorpPoura from 'app/entities/city-corp-poura/city-corp-poura.reducer';
import union from 'app/entities/union/union.reducer';
import ward from 'app/entities/ward/ward.reducer';
import postOffice from 'app/entities/post-office/post-office.reducer';
import country from 'app/entities/country/country.reducer';
import document from 'app/entities/document/document.reducer';
import documentType from 'app/entities/document-type/document-type.reducer';
import agency from 'app/entities/agency/agency.reducer';
import agencyType from 'app/entities/agency-type/agency-type.reducer';
import assignment from 'app/entities/assignment/assignment.reducer';
import designation from 'app/entities/designation/designation.reducer';
import passType from 'app/entities/pass-type/pass-type.reducer';
import pass from 'app/entities/pass/pass.reducer';
import zone from 'app/entities/zone/zone.reducer';
import gate from 'app/entities/gate/gate.reducer';
import lane from 'app/entities/lane/lane.reducer';
import accessProfile from 'app/entities/access-profile/access-profile.reducer';
import vehicle from 'app/entities/vehicle/vehicle.reducer';
import vehicleAssignment from 'app/entities/vehicle-assignment/vehicle-assignment.reducer';
import vehicleType from 'app/entities/vehicle-type/vehicle-type.reducer';
import entryLog from 'app/entities/entry-log/entry-log.reducer';
import agencyLicense from 'app/entities/agency-license/agency-license.reducer';
import department from 'app/entities/department/department.reducer';
import agencyLicenseType from 'app/entities/agency-license-type/agency-license-type.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  person,
  address,
  division,
  district,
  upazila,
  rMO,
  cityCorpPoura,
  union,
  ward,
  postOffice,
  country,
  document,
  documentType,
  agency,
  agencyType,
  assignment,
  designation,
  passType,
  pass,
  zone,
  gate,
  lane,
  accessProfile,
  vehicle,
  vehicleAssignment,
  vehicleType,
  entryLog,
  agencyLicense,
  department,
  agencyLicenseType,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
