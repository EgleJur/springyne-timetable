import { Collapse, Alert } from "@mui/material";
import { useState } from "react";

function EditModulePage() {
      const [success, setSuccess] = useState(false);
      const [failure, setFailure] = useState(false);

    return (
      <div className="mx-3">
        <h2 className="my-5">Redaguoti modulį</h2>
        <Collapse in={success}>
          <Alert
            onClose={() => {
              setSuccess(false);
            }}
            severity="success"
            className="mb-3"
          >
            Įrašas sėkmingai atnaujintas
          </Alert>
        </Collapse>
        <Collapse in={failure}>
          <Alert
            onClose={() => {
              setFailure(false);
            }}
            severity="error"
            className="mb-3"
          >
            Įrašo nepavyko atnaujinti
          </Alert>
        </Collapse>
      </div>
    );
}

export default EditModulePage;