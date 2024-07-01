import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AgencyLicense from './agency-license';
import AgencyLicenseDetail from './agency-license-detail';
import AgencyLicenseUpdate from './agency-license-update';
import AgencyLicenseDeleteDialog from './agency-license-delete-dialog';

const AgencyLicenseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AgencyLicense />} />
    <Route path="new" element={<AgencyLicenseUpdate />} />
    <Route path=":id">
      <Route index element={<AgencyLicenseDetail />} />
      <Route path="edit" element={<AgencyLicenseUpdate />} />
      <Route path="delete" element={<AgencyLicenseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AgencyLicenseRoutes;
