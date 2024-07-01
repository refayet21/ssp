import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Upazila from './upazila';
import UpazilaDetail from './upazila-detail';
import UpazilaUpdate from './upazila-update';
import UpazilaDeleteDialog from './upazila-delete-dialog';

const UpazilaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Upazila />} />
    <Route path="new" element={<UpazilaUpdate />} />
    <Route path=":id">
      <Route index element={<UpazilaDetail />} />
      <Route path="edit" element={<UpazilaUpdate />} />
      <Route path="delete" element={<UpazilaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UpazilaRoutes;
