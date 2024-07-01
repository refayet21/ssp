import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AgencyLicenseType from './agency-license-type';
import AgencyLicenseTypeDetail from './agency-license-type-detail';
import AgencyLicenseTypeUpdate from './agency-license-type-update';
import AgencyLicenseTypeDeleteDialog from './agency-license-type-delete-dialog';

const AgencyLicenseTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AgencyLicenseType />} />
    <Route path="new" element={<AgencyLicenseTypeUpdate />} />
    <Route path=":id">
      <Route index element={<AgencyLicenseTypeDetail />} />
      <Route path="edit" element={<AgencyLicenseTypeUpdate />} />
      <Route path="delete" element={<AgencyLicenseTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AgencyLicenseTypeRoutes;
