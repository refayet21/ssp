import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Division from './division';
import DivisionDetail from './division-detail';
import DivisionUpdate from './division-update';
import DivisionDeleteDialog from './division-delete-dialog';

const DivisionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Division />} />
    <Route path="new" element={<DivisionUpdate />} />
    <Route path=":id">
      <Route index element={<DivisionDetail />} />
      <Route path="edit" element={<DivisionUpdate />} />
      <Route path="delete" element={<DivisionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DivisionRoutes;
