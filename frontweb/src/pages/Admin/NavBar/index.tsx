import './styles.css';

const NavBar = () => {
    return (
        <nav className="admin-nav-container">
            <ul>
                <li>
                    <a className="admin-nav-item active" href="link"><p>Produtos</p></a>
                </li>
                <li>
                    <a className="admin-nav-item" href="link"><p>Categorias</p></a>
                </li>
                <li>
                    <a className="admin-nav-item" href="link"><p>Usuários</p></a>
                </li>
            </ul>
        </nav>
    );
};

export default NavBar;