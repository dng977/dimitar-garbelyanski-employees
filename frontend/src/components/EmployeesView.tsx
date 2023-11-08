import React, { useRef, useState } from "react";
import { Button, Col, Container, Form, FormGroup, Row, Stack, Table } from "react-bootstrap";

interface WorkedTogetherRecrod {
  employee1Id: number,
  employee2Id: number,
  projectId: number,
  daysWorked: number
};

export const EmployeesView = () => {
  const hiddenFileInput = useRef<HTMLInputElement>(null);

  const [uploadedFileName, setUploadedFileName] = useState<string | null>(null);

  const [data, setData] = React.useState<WorkedTogetherRecrod[]>([]);
  const [csvFile, setCsvFile] = React.useState(null);
  const [error, setError] = React.useState(null);


  function handleUpload(): void {
    hiddenFileInput.current?.click();
  }
  function handleDisplayFileDetails(e: any): void {
    hiddenFileInput.current?.files &&
      setUploadedFileName(hiddenFileInput.current.files[0].name);
    setCsvFile(e.target.files[0]);
  }

  function onGetData(event: any): void {
    const formData = new FormData();
    if (csvFile != null) {
      formData.append('file', csvFile);
      fetch("http://localhost:8080/api/employees/worked-together", {
        method: 'POST',
        headers: {
          ContentType: 'multipart/form-data'
        },
        body: formData
      })
        .then(response => response.json()).then(data => setData(data)).catch(err => {
          console.error(err);
          setError(err.message)
        })
    }
  }

  const groupList = (
    <><tr>
      <td>1</td>
      <td>Mark</td>
      <td>Otto</td>
      <td>@mdo</td>
    </tr><tr>
        <td>2</td>
        <td>Jacob</td>
        <td>Thornton</td>
        <td>@fat</td>
      </tr><tr>
        <td>3</td>
        <td colSpan={2}>Larry the Bird</td>
        <td>@twitter</td>
      </tr></>);



  return (
    <Container fluid className="center">
      <Stack gap={2} className="col-md-6 mx-auto align-items-center">
        <h3 className="float-center">Employees that have worked together</h3>
        <div className="float-center">
          <Form >
            <FormGroup className="mb-3">
              <input type={"file"}
                accept={".csv"}
                ref={hiddenFileInput}
                onChange={handleDisplayFileDetails}
                hidden
              />
              <Button variant={uploadedFileName ? "outline-success" : "outline-secondary"}
                onClick={handleUpload}>{uploadedFileName ? uploadedFileName : "Upload CSV"}</Button>
            </FormGroup>
            <FormGroup>
              <Button disabled={csvFile ? false : true} variant="outline-primary" onClick={onGetData}>Fetch data</Button>
              {error ? <span className="ms-2" style={{color:'red'}} >{error}</span> : null}
            </FormGroup>

          </Form>


        </div>

        <div className="table-responsive">
        <Table className="mt-4"  >
          <thead>
            <tr>
              <th >Employee 1 ID</th>
              <th >Employee 2 ID</th>
              <th>Project ID</th>
              <th >Days worked</th>
            </tr>
          </thead>
          <tbody>
            {data.map(row => (
              <tr>
                <td>{row.employee1Id}</td>
                <td>{row.employee2Id}</td>
                <td>{row.projectId}</td>
                <td>{row.daysWorked}</td>
              </tr>
            ))}
          </tbody>
        </Table>
        </div>

      </Stack>
    </Container>


  );
}