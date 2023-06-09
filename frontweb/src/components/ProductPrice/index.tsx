import { formatPrice } from 'util/formatters';
import './styles.css'

type Props = {
    price: number;
}

const ProductPrice = ( { price } : Props) => {
    return (
        <div>
            <div className="product-price-container">
                <span>R$</span>
                <h3>{formatPrice(price)}</h3>
            </div>
        </div>
    );
};

export default ProductPrice;