import { useState } from "react";
import { Alert, Collapse } from "@mui/material";
import { TextField } from "@mui/material";
import { Select, MenuItem} from "@mui/material";

function CreateShiftPage() {
  const [name, setName] = useState("");
  const [starts, setStarts] = useState(1);
  const [ends, setEnds] = useState(14);
  const [nameError, setNameError] = useState("");
  const [startError, setStartError] = useState(false); // added state for start error
  const [endError, setEndError] = useState(false); // added state for end error
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);

  const createNewShift = (e) => {
    e.preventDefault();
    setNameError("");
    setStartError(false);
    setEndError(false);
    if (name === "" || starts > ends) {
      if (name === "") {
        setNameError("Pavadinimas negali būti tuščias.");
      }
      if (starts > ends) {
        setStartError(true);
        setEndError(true);
      }
    } else {
      fetch('/api/v1/shifts', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(
          {
            name,
            starts,
            ends,
            visible: 1
          }
        )
      }).then((result) => {
        if (result.ok) {
          setStarts(1);
          setEnds(14);
          setName("");
          setSuccess(true);
          setFailure(false);
        } else {
          setFailure(true);
          setSuccess(false);
          setNameError("Įrašo nepavyko sukurti.");
        }
      });
    }
  }

  return (
    <div className="mx-3">
      <h2 className="my-5">Pridėti naują pamainą</h2>
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
          {nameError}
        </Alert>
      </Collapse>
      <form noValidate>
        <TextField
          error={!!nameError}
          onChange={(e) => setName(e.target.value)}
          value={name}
          id="create-shift-name"
          label="Pavadinimas"
          helperText="Pavadinimas turi būti unikalus ir yra privalomas"
          className="form-control mb-3"
          size="small"
          required
        />
        <label htmlFor="starts-select" className="me-2">
          Pradžia:
        </label>
        <Select
          id="starts-select"
          error={startError}
          value={starts}
          size="small"
          className="me-2"
          onChange={(e) => setStarts(parseInt(e.target.value))}
        >
          {Array.from(Array(14)).map((_, i) => (
            <MenuItem key={i+1} value={i+1}>
              {i+1}
            </MenuItem>
          ))}
        </Select>

        <label htmlFor="ends-select" className="me-2">
          Pabaiga:
        </label>
        <Select
          id="ends-select"
          error={endError}
          value={ends}
          size="small"
          className="me-2"
          onChange={(e) => setEnds(parseInt(e.target.value))}
        >
          {Array.from(Array(14)).map((_, i) => (
            <MenuItem key={i + 1} value={i + 1}>
              {i + 1}
            </MenuItem>
          ))}
        </Select>
        <div className="mt-4">

        </div>
        <button
          type="submit"
          className="btn btn-primary my-3"
          onClick={createNewShift}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateShiftPage;  