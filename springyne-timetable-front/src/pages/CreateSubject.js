import { useState } from "react";
import { Alert,Collapse } from "@mui/material";
import { TextField } from "@mui/material";

function CreateSubjectPage() {
  const [number, setNumber] = useState("");
  const [name, setName] = useState("");
  const [numberError, setNumberError] = useState("");
  const [nameError, setNameError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);

  const createNewModule = (e) => {
    e.preventDefault();
    setNumberError(false);
    setNameError(false);
    if (name === "" || number === "") {
      if (number === "") {
        setNumberError(true);
      }
      if (name === "") {
        setNameError(true);
      }
    } else {
      fetch("/api/v1/modules/", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          number,
          name,
        }),
      }).then((result) => {
        if (result.ok) {
          setNumber("");
          setName("");
          setSuccess(true);
          setFailure(false);
        } else {
          setFailure(true);
          setSuccess(false);
          setNumberError(true);
        }
      });
    }
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Pridėti naują modulį</h2>
      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai sukurtas
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
          Įrašo nepavyko sukurti
        </Alert>
      </Collapse>
      <form noValidate>
        <TextField
          error={!!numberError}
          onChange={(e) => setNumber(e.target.value)}
          value={number}
          id="create-module-number-with-error"
          label="Numeris"
          helperText="Numeris turi būti unikalus ir negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          error={!!nameError}
          onChange={(e) => setName(e.target.value)}
          value={name}
          id="create-module-number-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          required
        />
        <button
          type="submit"
          className="btn btn-primary"
          onClick={createNewModule}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateSubjectPage;
