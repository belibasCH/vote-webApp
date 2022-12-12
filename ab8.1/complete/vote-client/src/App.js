import Header from "./Header";

const App = () => (
    <div className="container-fluid">
        <Header
            title="Hello from JavaScript Client"
            lead="Server not available"
            isError={true}
        />
    </div>
);

export default App;
