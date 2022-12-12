const errorStyle = {
    color: "red",
};

const msgStyle = {
    color: "black",
};

const Header = ({ title, lead, isError }) => (
    <div className="rounded px-3 px-sm-4 py-3 py-sm-5 bg-light mb-3">
        <h1 className="display-3">{title}</h1>
        <p style={isError ? errorStyle : msgStyle}>{lead}</p>
    </div>
);

export default Header;
