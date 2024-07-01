import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AgencyType from './agency-type';
import AgencyTypeDetail from './agency-type-detail';
import AgencyTypeUpdate from './agency-type-update';
import AgencyTypeDeleteDialog from './agency-type-delete-dialog';

const AgencyTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AgencyType />} />
    <Route path="new" element={<AgencyTypeUpdate />} />
    <Route path=":id">
      <Route index element={<AgencyTypeDetail />} />
      <Route path="edit" element={<AgencyTypeUpdate />} />
      <Route path="delete" element={<AgencyTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AgencyTypeRoutes;
