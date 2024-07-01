import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Designation from './designation';
import DesignationDetail from './designation-detail';
import DesignationUpdate from './designation-update';
import DesignationDeleteDialog from './designation-delete-dialog';

const DesignationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Designation />} />
    <Route path="new" element={<DesignationUpdate />} />
    <Route path=":id">
      <Route index element={<DesignationDetail />} />
      <Route path="edit" element={<DesignationUpdate />} />
      <Route path="delete" element={<DesignationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DesignationRoutes;
